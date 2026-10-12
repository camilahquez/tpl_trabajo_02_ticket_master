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
        return asiento;
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
        this.asiento = asiento;
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

}
