package com.example.contactoscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormularioContactosScreen()
                }
            }
        }
    }
}

@Composable
fun FormularioContactosScreen() {
    var nombreInput by remember { mutableStateOf("") }
    var correoInput by remember { mutableStateOf("") }
    var telefonoInput by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf(false) }
    var errorCorreo by remember { mutableStateOf(false) }

    val listaContactos = remember { mutableStateListOf<Contacto>() }
    var siguienteId by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Contactos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo Nombre utilizando esNombreValido de tu Tarea 2
        OutlinedTextField(
            value = nombreInput,
            onValueChange = {
                nombreInput = it
                errorNombre = !ValidadorJava.esNombreValido(it)
            },
            label = { Text("Nombre Completo *") },
            isError = errorNombre,
            supportingText = {
                if (errorNombre) Text("El nombre es obligatorio")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Correo utilizando esCorreoValido de tu Tarea 2
        OutlinedTextField(
            value = correoInput,
            onValueChange = {
                correoInput = it
                errorCorreo = !ValidadorJava.esCorreoValido(it)
            },
            label = { Text("Correo Electrónico *") },
            isError = errorCorreo,
            supportingText = {
                if (errorCorreo) Text("Introduce un correo válido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Teléfono (Opcional)
        OutlinedTextField(
            value = telefonoInput,
            onValueChange = { telefonoInput = it },
            label = { Text("Teléfono (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Adaptado rigurosamente a tus validadores reales de la Tarea 2
                val esNombreValido = ValidadorJava.esNombreValido(nombreInput)
                val esCorreoValido = ValidadorJava.esCorreoValido(correoInput)

                errorNombre = !esNombreValido
                errorCorreo = !esCorreoValido

                if (esNombreValido && esCorreoValido) {
                    val telfNuevo = if (telefonoInput.isBlank()) null else telefonoInput

                    listaContactos.add(Contacto(id = siguienteId, nombre = nombreInput, correo = correoInput, telefono = telfNuevo))
                    siguienteId++

                    nombreInput = ""
                    correoInput = ""
                    telefonoInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Contacto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Contactos Agregados",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaContactos) { contacto ->
                ContactoCard(contacto = contacto)
            }
        }
    }
}

@Composable
fun ContactoCard(contacto: Contacto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: #${contacto.id} - ${contacto.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Correo: ${contacto.correo}", style = MaterialTheme.typography.bodyMedium)

            val textoTelefono = contacto.telefono ?: "No registrado"
            Text(text = "Teléfono: $textoTelefono", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
