
package org.example.servicios;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
//Se encarga de generar códigos QR a partir de información proporcionada en formato de texto, utilizando la biblioteca ZXing. Devuelve el código generado como una imagen que puede utilizarse en otros componentes de la aplicación, como la generación de entradas en PDF.
public class GeneradorQRService {
    //Recibe una cadena de texto y la codifica en un código QR de 250 × 250 píxeles. Convierte la matriz generada en una imagen de tipo BufferedImage para facilitar su incorporación en documentos PDF u otros elementos de la aplicación. Lanza una excepción WriterException si ocurre un error durante la generación del código QR.
    public BufferedImage generarQR(String informacion)
            throws WriterException {

        QRCodeWriter generador = new QRCodeWriter();

        BitMatrix matriz = generador.encode(
                informacion,
                BarcodeFormat.QR_CODE,
                250,
                250
        );

        return MatrixToImageWriter.toBufferedImage(matriz);
    }
}
