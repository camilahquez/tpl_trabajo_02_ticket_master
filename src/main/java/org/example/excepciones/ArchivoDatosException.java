package org.example.excepciones;

public class ArchivoDatosException extends Exception {
    public ArchivoDatosException(String mensaje) {
        super(mensaje);
    }
    public ArchivoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}