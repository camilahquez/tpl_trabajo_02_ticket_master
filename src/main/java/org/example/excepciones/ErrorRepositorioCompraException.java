package org.example.excepciones;

public class ErrorRepositorioCompraException extends  RuntimeException{
    public ErrorRepositorioCompraException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
