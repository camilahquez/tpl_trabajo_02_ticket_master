package org.example.excepciones;

/** Error al generar la gráfica o el PDF del reporte de ventas. */
public class ErrorReporteException extends RuntimeException {
    public ErrorReporteException(String mensaje) {
        super(mensaje);
    }

    public ErrorReporteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
