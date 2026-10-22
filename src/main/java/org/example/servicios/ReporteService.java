package org.example.servicios;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.example.excepciones.ErrorReporteException;
import org.example.modelo.Asiento;
import org.example.modelo.Compra;
import org.example.modelo.Eventos;
import org.example.repositorios.CompraJSONRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
//Se encarga de generar un reporte de ventas por evento en formato PDF. Consulta las compras registradas, calcula la cantidad de boletas vendidas, los ingresos y el porcentaje de ocupación de cada evento. Genera una gráfica de barras con los ingresos y presenta los resultados en un documento PDF.
public class ReporteService {

    private final EventosService eventosService;
    private final AsientoService asientoService;
    private final CompraJSONRepository compraRepositorio;
    private final GraficaService graficaService;
//Inicializa el servicio de reportes mediante la inyección de las dependencias necesarias para consultar los eventos, obtener información de los asientos, recuperar las compras registradas y generar gráficas.
    public ReporteService(EventosService eventosService, AsientoService asientoService, CompraJSONRepository compraRepositorio, GraficaService graficaService) {
        this.eventosService = eventosService;
        this.asientoService = asientoService;
        this.compraRepositorio = compraRepositorio;
        this.graficaService = graficaService;
    }
    //Genera un reporte de ventas por evento a partir de las compras almacenadas. Calcula la cantidad de boletas vendidas, los ingresos obtenidos y el porcentaje de ocupación de cada evento. Genera una gráfica de barras con los ingresos por evento e incorpora los resultados en un documento PDF. Guarda el archivo generado en la carpeta reportes, con el nombre ventas_por_evento.pdf. Si ocurre un error durante la creación o el almacenamiento del documento, lanza una excepción ErrorReporteException.
    public void generarReporte() {
        List<Compra> compras = compraRepositorio.obtenerTodas();

        List<String> nombres = new ArrayList<>();   // para el eje X de la gráfica
        List<Double> ingresos = new ArrayList<>();  // para las barras
        List<String> lineas = new ArrayList<>();    // texto que va debajo de la gráfica

        // 1. Calcular los datos de cada evento
        for (Eventos evento : eventosService.obtenerEventos()) {
            int boletasVendidas = 0;
            double ingresosEvento = 0;

            for (Compra compra : compras) {
                for (Asiento asiento : compra.getAsientos()) {
                    if (asiento.getIdEvento().equalsIgnoreCase(evento.getIdEvento())) {
                        boletasVendidas++;
                        ingresosEvento += asiento.getPrecio();
                    }
                }
            }

            double ocupacion = asientoService.calcularPorcentajeOcupacion(evento.getIdEvento());
            nombres.add(evento.getNombreEvento());
            ingresos.add(ingresosEvento);
            lineas.add(evento.getNombreEvento() + " | " + boletasVendidas + " boletas | $"
                    + String.format("%.0f", ingresosEvento) + " | "
                    + String.format("%.1f", ocupacion) + "% ocupación");
        }
        byte[] grafica = graficaService.generarGraficaBarras(
                "Ingresos por evento", "Evento", "Ingresos (COP)", nombres, ingresos);

        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);
            PDImageXObject imagen = PDImageXObject.createFromByteArray(documento, grafica, "grafica");

            try (PDPageContentStream pdf = new PDPageContentStream(documento, pagina)) {
                pdf.drawImage(imagen, 50, 450, 495, 283);

                pdf.beginText();
                pdf.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                pdf.setLeading(18);
                pdf.newLineAtOffset(50, 420);
                for (String linea : lineas) {
                    pdf.showText(linea);
                    pdf.newLine();
                }
                pdf.endText();
            }
            Files.createDirectories(Path.of("reportes"));
            documento.save("reportes/ventas_por_evento.pdf");
        } catch (IOException e) {
            throw new ErrorReporteException("No se pudo generar el reporte PDF.", e);
        }
    }
}
