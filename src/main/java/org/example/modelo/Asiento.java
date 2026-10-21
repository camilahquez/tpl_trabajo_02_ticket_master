package org.example.modelo;

public class Asiento {
    private String idEvento;
    private String codigoAsiento;
    private String localidad;
    private String fila;
    private int numero;
    private double precio;
    private boolean ocupado; // Controla si ya fue vendido

    public Asiento(String idEvento, String codigoAsiento, String localidad, String fila, int numero, double precio) {
        this.idEvento = idEvento;
        this.codigoAsiento = codigoAsiento;
        this.localidad = localidad;
        this.fila = fila;
        this.numero = numero;
        this.precio = precio;
        this.ocupado = false; // Por defecto está disponible
    }
    public Asiento(String idEvento, String codigoAsiento, String localidad,
                   String fila, int numero, double precio, boolean ocupado) {
        this.idEvento = idEvento;
        this.codigoAsiento = codigoAsiento;
        this.localidad = localidad;
        this.fila = fila;
        this.numero = numero;
        this.precio = precio;
        this.ocupado = ocupado;
    }

    // Getters
    public String getIdEvento() { return idEvento; }
    public String getCodigoAsiento() { return codigoAsiento; }
    public String getLocalidad() { return localidad; }
    public String getFila() { return fila; }
    public int getNumero() { return numero; }
    public double getPrecio() { return precio; }
    public boolean isOcupado() { return ocupado; }

    // Setters corregidos correctamente (con parámetros)
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }
    public void setCodigoAsiento(String codigoAsiento) { this.codigoAsiento = codigoAsiento; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public void setFila(String fila) { this.fila = fila; }
    public void setNumero(int numero) { this.numero = numero; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setOcupado(boolean ocupado) { this.ocupado = ocupado; }

    @Override
    public String toString() {
        String estado = ocupado ? "[VENDIDO]" : "[DISPONIBLE]";
        return String.format("%s - %s (Fila: %s, Núm: %d) | Precio: $%.2f %s",
                codigoAsiento, localidad, fila, numero, precio, estado);
    }
}