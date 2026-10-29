package com.example.contactoscompose;

public class ValidadorJava {

    public static boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    public static boolean esCorreoValido(String correo) {
        if (correo == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo.matches(regex);
    }

    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return true; // Es válido porque el campo es opcional
        }
        return telefono.matches("\\d{8,15}"); // Si tiene texto, debe tener de 8 a 15 números
    }
}
