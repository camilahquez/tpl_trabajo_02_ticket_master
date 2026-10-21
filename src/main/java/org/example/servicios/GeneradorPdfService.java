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
import java.util.stream.Collectors;
import org.example.modelo.Eventos;
//Se encarga de generar las entradas en formato PDF a partir de una compra registrada. Incluye la información del comprador, el evento, el asiento, la imagen del evento y un código QR con los datos de la compra. Utiliza la biblioteca Apache PDFBox para crear los documentos.
public class GeneradorPdfService {
    private final EventosService eventosService;
    private final GeneradorQRService generadorQRService;
    //Inicializa el servicio de generación de PDF mediante la inyección de las dependencias EventosService y GeneradorQRService, necesarias para consultar información de los eventos y generar los códigos QR.
    public GeneradorPdfService(EventosService eventosService, GeneradorQRService generadorQRService) {
        this.eventosService = eventosService;
        this.generadorQRService = generadorQRService;
    }
//Escribe un texto en una página del PDF, utilizando las coordenadas especificadas para determinar su posición, el tamaño de fuente indicado y la opción de aplicar negrita.
  private  void escribirTexto(PDPageContentStream contenido, String texto, float x, float y, int tamano, boolean negrita) throws IOException {
      PDType1Font fuente = new PDType1Font(negrita ? Standard14Fonts.FontName.HELVETICA_BOLD : Standard14Fonts.FontName.HELVETICA);
      contenido.beginText();
      contenido.setFont(fuente, tamano);
      contenido.newLineAtOffset(x, y);
      contenido.showText(texto);
      contenido.endText();
  }
  //Agrega el encabezado visual de la entrada al PDF, incluyendo el nombre de la ticketera, el nombre del evento y el texto que identifica el documento como una entrada oficial. Aplica colores y estilos para diferenciar esta sección del resto del contenido.
    private void agregarEncabezado(PDPageContentStream contenido, Compra compra) throws IOException {
        contenido.setNonStrokingColor(20 / 255f, 39 / 255f, 71 / 255f);
        contenido.addRect(0, 700, 595, 142);
        contenido.fill();
        contenido.setNonStrokingColor(1f, 1f, 1f);
        escribirTexto(contenido, "TICKET MASTER", 40, 800, 12, true);
        escribirTexto(contenido, compra.getNombreEvento(), 40, 755, 22, true);

        escribirTexto(contenido, "ENTRADA OFICIAL", 40, 725, 11, false);
    }
    //Incorpora a la página del PDF la información principal de la entrada, incluyendo el nombre del comprador, el evento, el código del asiento y el identificador de la compra.
    private void agregarInformacionCompra(PDPageContentStream contenido, Compra compra, Asiento asiento) throws IOException
    {
        contenido.setNonStrokingColor(0f, 0f, 0f);
        escribirTexto(contenido, "INFORMACION DE TU ENTRADA", 40, 660, 16, true);
        escribirTexto(contenido, "Comprador: " + compra.getNombreComprador(), 40, 620, 12, false);
        escribirTexto(contenido, "Evento: " + compra.getNombreEvento(), 40, 590, 12, false);
        escribirTexto(contenido, "Asiento: " + asiento.getCodigoAsiento(), 40, 550, 16, true);
        escribirTexto(contenido, "Compra: " + compra.getId(), 40, 520, 10, false);

    }
//Carga la imagen asociada al evento desde los recursos del proyecto y la incorpora al PDF en la posición y dimensiones establecidas. Lanza una excepción IOException si la imagen no se encuentra o no puede leerse.
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

            contenido.drawImage(imagenPDF, 330, 520, 220, 140);
        }
    }
    //Genera un código QR con información de la compra, incluyendo su identificador, el nombre del comprador, el identificador del evento, los asientos adquiridos y el valor total. Incorpora el código QR a la página del PDF para facilitar la consulta de los datos de la compra.
    private void agregarQR(PDDocument documento, PDPageContentStream contenido, Compra compra) throws Exception {

        String asientos = compra.getAsientos().stream().map(Asiento::getCodigoAsiento).collect(Collectors.joining(","));
        Eventos evento = eventosService.buscarEventoPorNombre(compra.getNombreEvento());
        String idEvento = evento != null ? evento.getIdEvento() : compra.getNombreEvento();
        String informacionQR = "ID_COMPRA=" + compra.getId() + " | TITULAR=" + compra.getNombreComprador() + " | EVENTO=" + idEvento + " | ASIENTOS=" + asientos + " | TOTAL=" + compra.getTotal();
        BufferedImage imagenQR = generadorQRService.generarQR(informacionQR);
        PDImageXObject qrPDF = LosslessFactory.createFromImage(documento, imagenQR);
        contenido.drawImage(qrPDF, 385, 370, 130, 130);
    }
//Crea una página de tamaño A4 para una entrada individual y organiza sus elementos visuales. Agrega el encabezado, la información de la compra, la imagen del evento y el código QR, utilizando los métodos auxiliares de la clase.
    private void crearPaginaEntrada(PDDocument documento, Compra compra, Asiento asiento) throws Exception {

        PDPage pagina = new PDPage(PDRectangle.A4);
        documento.addPage(pagina);

        try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {

            agregarEncabezado(contenido, compra);
            agregarInformacionCompra(contenido, compra, asiento);
            String rutaImagen = eventosService.buscarRutaImagen(compra.getNombreEvento());
            agregarImagenEvento(documento, contenido, rutaImagen);
            agregarQR(documento, contenido, compra);
        }
    }
    //Genera un documento PDF con una entrada individual por cada asiento incluido en la compra. Crea las páginas correspondientes, guarda el archivo en la carpeta pdf-generados dentro de los recursos del proyecto y devuelve la ruta absoluta del documento generado. Si ocurre un error durante la creación o el almacenamiento, propaga la excepción correspondiente.
    public String generarPDF(Compra compra) throws Exception {

        try (PDDocument documento = new PDDocument()) {

            for (Asiento asiento : compra.getAsientos()) {
                crearPaginaEntrada(documento, compra, asiento);
            }
            Path carpeta = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "pdf-generados");
            java.nio.file.Files.createDirectories(carpeta);
            String nombreArchivo = "entrada_" + compra.getId() + ".pdf";
            Path rutaPDF = carpeta.resolve(nombreArchivo);
            documento.save(rutaPDF.toFile());
            return rutaPDF.toAbsolutePath().toString();
        }
    }

}
