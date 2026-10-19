package org.example.servicios;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.modelo.Asiento;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraCompraServiceTest {

    @Test
    void calcularServicioDebeCalcularElDiezPorCiento() {

        double subtotal = 80000;
        double resultado = CalculadoraCompraService.calcularServicio(subtotal);
        assertEquals(8000.0, resultado, 0.001);
    }

    @Test
    void calcularTotalDebeSumarSubtotalYServicio() {


        double subtotal = 80000;
        double servicio = 8000;

        double resultado = CalculadoraCompraService.calcularTotal(subtotal, servicio);
        assertEquals(88000.0, resultado, 0.001);
    }
    @Test
    void calcularSubtotalDebeSumarLosPreciosDeLosAsientos() {
        Asiento asiento = new Asiento("001D", "00111F", "ALTA", "60", 80, 150000.0);
        Asiento asiento1 = new Asiento("001D", "001211F", "ALTA", "60", 85, 150000.0);
        List<Asiento> asientos = new ArrayList<>();
        asientos.add(asiento);
        asientos.add(asiento1);
        double resultado = CalculadoraCompraService.carcularSubtotal(asientos);
        assertEquals(300000.0, resultado, 0.001);
    }
}
