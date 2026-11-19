package com.example.backend_veterinaria.exception;

public class NoAutorizadoException extends RuntimeException {

    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
