package domain.entities.QR;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import configuracion.Configuracion;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class GeneradorDeCodigoQR {
    private String QR_RUTA;

    public GeneradorDeCodigoQR() {
        QR_RUTA = Configuracion.INSTANCE.getPropiedad("qr.ruta");
    }

    public String generarCodigoQR(String texto, int ancho, int alto, String nombreArchivo) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, ancho, alto);
        Path path = FileSystems.getDefault().getPath(QR_RUTA,  nombreArchivo + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return path.toString();
    }
}
