package org.example.servicios;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.example.modelo.Compra;
import org.example.modelo.Asiento;
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import java.nio.file.Path;

public class GeneradorPdfService {
    private final EventosService eventosService;
    private final GeneradorQRService generadorQRService;
    public GeneradorPdfService(EventosService eventosService, GeneradorQRService generadorQRService) {
        this.eventosService = eventosService;
        this.generadorQRService = generadorQRService;
    }

  private  void escribirTexto(PDPageContentStream contenido, String texto, float x, float y, int tamano, boolean negrita) throws IOException {
      PDType1Font fuente = new PDType1Font(negrita ? Standard14Fonts.FontName.HELVETICA_BOLD : Standard14Fonts.FontName.HELVETICA);
      contenido.beginText();
      contenido.setFont(fuente, tamano);
      contenido.newLineAtOffset(x, y);
      contenido.showText(texto);
      contenido.endText();
  }
    private void agregarEncabezado(PDPageContentStream contenido, Compra compra) throws IOException {
        contenido.setNonStrokingColor(20 / 255f, 39 / 255f, 71 / 255f);
        contenido.addRect(0, 700, 595, 142);
        contenido.fill();
        contenido.setNonStrokingColor(1f, 1f, 1f);
        escribirTexto(contenido, "TICKET MASTER", 40, 800, 12, true);
        escribirTexto(contenido, compra.getNombreEvento(), 40, 755, 22, true);

        escribirTexto(contenido, "ENTRADA OFICIAL", 40, 725, 11, false);
    }
    private void agregarInformacionCompra(PDPageContentStream contenido, Compra compra, Asiento asiento) throws IOException
    {
        escribirTexto(contenido, "INFORMACION DE TU ENTRADA", 40, 660, 16, true);
        escribirTexto(contenido, "Comprador: " + compra.getNombreComprador(), 40, 620, 12, false);
        escribirTexto(contenido, "Evento: " + compra.getNombreEvento(), 40, 590, 12, false);
        escribirTexto(contenido, "Asiento: " + asiento.getCodigoAsiento(), 40, 550, 16, true);
        escribirTexto(contenido, "Compra: " + compra.getId(), 40, 520, 10, false);
        contenido.setStrokingColor(190 / 255f, 200 / 255f, 215 / 255f);
        contenido.moveTo(40, 490);
        contenido.lineTo(555, 490);
        contenido.stroke();
    }
    //agregarImagenEvento()
    private void agregarImagenEvento(PDDocument documento, PDPageContentStream contenido, String rutaImagen) throws IOException {

        try (InputStream entrada = GeneradorPdfService.class.getResourceAsStream(rutaImagen)) {

            if (entrada == null) {
                throw new IOException("No se encontró la imagen: " + rutaImagen);
            }
            BufferedImage imagenOriginal = ImageIO.read(entrada);

            if (imagenOriginal == null) {
                throw new IOException("No se pudo leer la imagen: " + rutaImagen);
            }

            PDImageXObject imagenPDF = LosslessFactory.createFromImage(documento, imagenOriginal);

            contenido.drawImage(imagenPDF, 300, 350, 245, 125);
        }
    }
    private void agregarQR(PDDocument documento, PDPageContentStream contenido, Compra compra, Asiento asiento) throws Exception {

        String informacionQR = compra.getId() + "|" + asiento.getCodigoAsiento();
        BufferedImage imagenQR = generadorQRService.generarQR(informacionQR);
        PDImageXObject qrPDF = LosslessFactory.createFromImage(documento, imagenQR);
        contenido.drawImage(qrPDF, 400, 250, 100, 100);
    }

    private void crearPaginaEntrada(PDDocument documento, Compra compra, Asiento asiento) throws Exception {

        PDPage pagina = new PDPage(PDRectangle.A4);
        documento.addPage(pagina);

        try (PDPageContentStream contenido =
                     new PDPageContentStream(documento, pagina)) {

            agregarEncabezado(contenido, compra);
            agregarInformacionCompra(contenido, compra, asiento);
            String rutaImagen = eventosService.buscarRutaImagen(compra.getNombreEvento());
            agregarImagenEvento(documento, contenido, rutaImagen);
            agregarQR(documento, contenido, compra, asiento);
        }
    }
    public String generarPDF(Compra compra) throws Exception {

        try (PDDocument documento = new PDDocument()) {

            for (Asiento asiento : compra.getAsientos()) {
                crearPaginaEntrada(documento, compra, asiento);
            }
            Path carpeta = Path.of("pdf-generados");
            java.nio.file.Files.createDirectories(carpeta);
            String nombreArchivo = "entrada_" + compra.getId() + ".pdf";
            Path rutaPDF = carpeta.resolve(nombreArchivo);
            documento.save(rutaPDF.toFile());
            return rutaPDF.toAbsolutePath().toString();
        }
    }

}
