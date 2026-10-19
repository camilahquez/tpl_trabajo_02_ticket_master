package org.example.servicios;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.repositorios.CompraJSONRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class ComprasServiceTest {

    @Mock
    private CompraJSONRepository repositorio;

    @Mock
    private ValidadorCompraService validadorCompraService;

    @Mock
    private AsientoService asientoService;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private GeneradorPdfService generadorPdfService;

    @InjectMocks
    private ComprasService comprasService;

    @Test
    void confirmarCompraDebeGuardarCompraCuandoAsientoEstaDisponible() throws Exception {

        Asiento asiento = new Asiento("001D", "00111F", "ALTA", "60", 80, 150000.0);
        List<Asiento> asientos = new ArrayList<>();
        asientos.add(asiento);
        Compra compra = new Compra("compra-test-001", "Ana Perez", "123456789", "ana@example.com", "Concierto", asientos, 150000.0, 15000.0, 165000.0);
        boolean resultado = comprasService.confirmarCompra(compra);
        assertTrue(resultado);
        verify(asientoService).reservarAsiento(asiento.getIdEvento(), asiento.getCodigoAsiento());
        verify(repositorio).guardar(compra);
        verify(generadorPdfService).generarPDF(compra);
    }

    @Test
    void confirmarCompraDebeRechazarCompraCuandoAsientoEstaVendido() throws Exception {

        Asiento asiento = new Asiento("001D", "00111F", "ALTA", "60", 80, 150000.0);

        List<Asiento> asientos = new ArrayList<>();
        asientos.add(asiento);
        Compra compra = new Compra("compra-test-002", "Ana Perez", "123456789", "ana@example.com", "Concierto", asientos, 150000.0, 15000.0, 165000.0);
        org.mockito.Mockito.doThrow(new org.example.excepciones.AsientoNoDisponibleException("El asiento ya está vendido")).when(asientoService).reservarAsiento(asiento.getIdEvento(), asiento.getCodigoAsiento());
        boolean resultado = comprasService.confirmarCompra(compra);
        org.junit.jupiter.api.Assertions.assertFalse(resultado);
        org.mockito.Mockito.verify(repositorio, org.mockito.Mockito.never()).guardar(compra);
        org.mockito.Mockito.verify(generadorPdfService, org.mockito.Mockito.never()).generarPDF(compra);
    }

}
