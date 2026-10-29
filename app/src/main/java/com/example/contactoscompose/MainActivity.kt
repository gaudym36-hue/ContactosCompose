package com.example.contactoscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.contactoscompose.ui.theme.ContactosComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactosComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaContactos()
                }
            }
        }
    }
}

@Composable
fun PantallaContactos() {
    // Estados para los textos del Formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // Estados de Error de los campos
    var errorNombre by remember { mutableStateOf(false) }
    var errorCorreo by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }

    // Lista mutable reactiva para almacenar los contactos agregados
    val listaContactos = remember { mutableStateListOf<Contacto>() }
    var contadorId by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Formulario de Contactos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo Nombre Completo
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = !ValidadorJava.esNombreValido(it)
            },
            label = { Text("Nombre Completo *") },
            isError = errorNombre,
            supportingText = {
                if (errorNombre) Text("El nombre no puede quedar vacío.")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Correo Electrónico
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                errorCorreo = !ValidadorJava.esCorreoValido(it)
            },
            label = { Text("Correo Electrónico *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorCorreo,
            supportingText = {
                if (errorCorreo) Text("Formato de correo no válido (ejemplo@correo.com).")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Teléfono (Opcional - Anulable)
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                errorTelefono = !ValidadorJava.esTelefonoValido(it)
            },
            label = { Text("Teléfono (Opcional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = errorTelefono,
            supportingText = {
                if (errorTelefono) Text("Debe ser numérico y contener entre 8 y 15 dígitos.")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Registrar Contacto
        Button(
            onClick = {
                // Validación final de campos usando la clase Java
                val nValido = ValidadorJava.esNombreValido(nombre)
                val cValido = ValidadorJava.esCorreoValido(correo)
                val tValido = ValidadorJava.esTelefonoValido(telefono)

                errorNombre = !nValido
                errorCorreo = !cValido
                errorTelefono = !tValido

                if (nValido && cValido && tValido) {
                    // Si el teléfono se dejó vacío, se guarda como null en nuestra data class
                    val tGuardar = if (telefono.trim().isEmpty()) null else telefono

                    listaContactos.add(
                        Contacto(id = contadorId++, nombre = nombre, correo = correo, telefono = tGuardar)
                    )

                    // MEJORA: Limpiar campos automáticamente al guardar con éxito
                    nombre = ""
                    correo = ""
                    telefono = ""

                    // Resetear estados de error visuales tras éxito
                    errorNombre = false
                    errorCorreo = false
                    errorTelefono = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Contacto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Contactos Registrados (${listaContactos.size})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // LazyColumn optimizada para renderizar las tarjetas (Cards) de forma eficiente
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaContactos) { contacto ->
                TarjetaContacto(contacto = contacto)
            }
        }
    }
}

@Composable
fun TarjetaContacto(contacto: Contacto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = contacto.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Correo: ${contacto.correo}", style = MaterialTheme.typography.bodyMedium)
            // Despliegue seguro y controlado de la variable anulable (null safety)
            Text(
                text = "Teléfono: ${contacto.telefono ?: "No especificado"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

