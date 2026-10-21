package org.example.servicios;
import org.example.modelo.Asiento;

import java.util.List;
// Gestiona el calculo del precio de la entrada
public class CalculadoraCompraService {
    //Calcula el costo bruto de los aisentos sumando el precio individual.
    public static double carcularSubtotal(List<Asiento> asientosSeleccionados){
        double subtotal = 0;
        for (Asiento asiento : asientosSeleccionados){
            subtotal += asiento.getPrecio();
        }
        return subtotal;
    }
    //Hace el calculo del costo del servicio que es el 10% de la compra.
    public static double calcularServicio(double subtotal){
       double servicio = 0.10 * subtotal;
       return servicio;
    }
// suma el valor bruto y el servicio para entregarnos el valor a pagar.
    public static double calcularTotal(double subtotal, double service){
        double total = subtotal +service;
        return total;
    }
}
