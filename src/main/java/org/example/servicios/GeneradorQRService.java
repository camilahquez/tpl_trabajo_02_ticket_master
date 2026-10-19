
package org.example.servicios;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
public class GeneradorQRService {
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
