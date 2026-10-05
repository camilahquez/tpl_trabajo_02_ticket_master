package org.example.modelo;

public class Compras {
    private String id;
    private String nombreComprador;
    private String documentoComprador;
    private String correoCoprador;
    private String nombreEvento;
    private String asiento;
    private double subtotal;
    private  double servicio;
    private double total;

    public Compras(String id, String nombreComprador, String documentoComprador, String correoCoprador, String nombreEvento, String asiento, double subtotal, double servicio, double total){
     this.id = id;
     this.nombreComprador = nombreComprador;
     this.documentoComprador = documentoComprador;
     this.correoCoprador = correoCoprador;
     this.nombreEvento = nombreEvento;
     this.asiento = asiento;
     this.subtotal = subtotal;
     this.servicio = servicio;
     this.total = total;
    }
}
