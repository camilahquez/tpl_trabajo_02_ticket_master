package org.example.servicios;
import org.example.modelo.Asiento;

import java.util.List;

public class CalculadoraCompraService {
    public static double carcularSubtotal(List<Asiento> asientosSeleccionados){
        double subtotal = 0;
        for (Asiento asiento : asientosSeleccionados){
            subtotal += asiento.getPrecio();
        }
        return subtotal;

    }
    public static double calcularServicio(double subtotal){
       double servicio = 0.10 * subtotal;
       return servicio;
    }

    public static double calcularTotal(double subtotal, double service){
        double total = subtotal +service;
        return total;
    }
}
