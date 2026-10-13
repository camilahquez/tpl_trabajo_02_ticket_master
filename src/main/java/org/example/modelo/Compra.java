package org.example.modelo;

import java.util.List;

public class Compra {
    private String id;
    private String nombreComprador;
    private String documentoComprador;
    private String correoCoprador;
    private String nombreEvento;
    private List<Asiento> asientos;
    private double subtotal;
    private  double servicio;
    private double total;

    public Compra(String id, String nombreComprador, String documentoComprador, String correoCoprador, String nombreEvento, String asientos, double subtotal, double servicio, double total){
     this.id = id;
     this.nombreComprador = nombreComprador;
     this.documentoComprador = documentoComprador;
     this.correoCoprador = correoCoprador;
     this.nombreEvento = nombreEvento;
     this.asientos = asientos;
     this.subtotal = subtotal;
     this.servicio = servicio;
     this.total = total;
    }

    public String getId(){
        return id;
    }
    public String getNombreComprador(){
        return nombreComprador;
    }
    public String getDocumentoComprador(){
        return documentoComprador;
    }
    public String getCorreoCoprador(){
        return correoCoprador;
    }
    public String getNombreEvento(){
        return nombreEvento;
    }
    public  String getAsiento(){
        return asientos;
    }
    public  double getSubtotal(){
        return subtotal;
    }
    public double getServicio(){
        return servicio;
    }
    public double getTotal(){
        return total;
    }
    public void setId(){
        this.id = id;
    }
    public void setNombreComprador(){
        this.nombreComprador = nombreComprador;
    }
    public void setDocumentoComprador(){
        this.correoCoprador = documentoComprador;
    }
    public void setCorreoCoprador(){
        this.correoCoprador = correoCoprador;
    }
    public void setNombreEvento(){
        this.nombreEvento = nombreEvento;
    }
    public  void setAsiento(){
        this.asientos = asientos;
    }
    public  void setSubtotal(){
        this.subtotal = subtotal;
    }
    public void setServicio(){
        this.servicio = servicio;
    }
    public void setTotal(){
        this.total = total;
    }
    @Override
    public String  toString(){
        return "Numero compra" + id +
                "Nombre del comprador" + nombreComprador +
                "evento" + nombreEvento +
                "asiento" + asientos +
                "cedula comprdor" + documentoComprador +
                "correo comprador" + correoCoprador +
                "subtotal" + subtotal +
                "servicio" + servicio +
                "total" + total;
     }


}
