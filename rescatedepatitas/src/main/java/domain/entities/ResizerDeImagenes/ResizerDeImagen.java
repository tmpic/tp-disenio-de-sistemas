package domain.entities.ResizerDeImagenes;

import configuracion.Configuracion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResizerDeImagen {

    private int ancho;
    private int alto;

    public ResizerDeImagen() {
        this.ancho = Integer.parseInt(Configuracion.INSTANCE.getPropiedad("imagen.ancho"));
        this.alto = Integer.parseInt(Configuracion.INSTANCE.getPropiedad("imagen.alto"));
    }

    public ResizerDeImagen(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    public void normalizarImagen(String path) throws IOException {
        File inputFile = new File(path);
        BufferedImage inputImage = ImageIO.read(inputFile);
        BufferedImage outputImage = new BufferedImage(
                this.ancho,
                this.alto,
                inputImage.getType()
        );

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, this.ancho, this.alto, null);
        g2d.dispose();

        String formatName = path.substring(path.lastIndexOf(".") + 1);
        ImageIO.write(outputImage, formatName, new File(path));
    }


}



