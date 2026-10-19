package org.example.excepciones;

public class ErrorAuditoriaException extends RuntimeException {
    public ErrorAuditoriaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
