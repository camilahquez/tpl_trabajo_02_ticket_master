
package org.example.servicios;

import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import org.example.modelo.Eventos;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneradorPdfServiceTest {

    @Test
    void generarPDFDebeCrearArchivoValido() throws Exception {
        Eventos evento = new Eventos("001D", "Concierto", LocalDate.of(2026, 12, 20), LocalTime.of(19, 0), "Medellin", "Musica", "/imagenes-eventos/evento.png");
        List<Eventos> listaEventos = new ArrayList<>();
        listaEventos.add(evento);
        EventosService eventosService = new EventosService(listaEventos);
        GeneradorQRService generadorQRService = new GeneradorQRService();
        GeneradorPdfService generadorPdfService = new GeneradorPdfService(eventosService, generadorQRService);
        Asiento asiento = new Asiento("001D", "00111F", "ALTA", "60", 80, 150000.0);
        Asiento asiento1 = new Asiento("001D", "00111Fl", "ALTA", "60", 83, 150000.0);
        List<Asiento> asientos = new ArrayList<>();
        asientos.add(asiento);
        asientos.add(asiento1);
        Compra compra = new Compra("prueba-pdf-001", "Ana Perez", "123456789", "ana@example.com", "Concierto", asientos, 150000.0, 15000.0, 165000.0);
        String rutaGenerada = generadorPdfService.generarPDF(compra);
        System.out.println("PDF generado en: " + rutaGenerada);
        Path archivoPDF = Path.of(rutaGenerada);
        assertTrue(Files.exists(archivoPDF));
        assertTrue(Files.size(archivoPDF) > 0);
        try (PDDocument documento = org.apache.pdfbox.Loader.loadPDF(archivoPDF.toFile())) {
            assertEquals(asientos.size(), documento.getNumberOfPages());
        }

    }
}