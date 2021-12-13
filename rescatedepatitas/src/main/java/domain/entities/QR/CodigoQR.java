package domain.entities.QR;

import configuracion.Configuracion;
import domain.entities.mascota.Mascota;

public class CodigoQR {
    private int ancho;
    private int alto;
    private String url;

    public CodigoQR(Mascota mascota) {
        this.mascota = mascota;
        ancho = Integer.parseInt(Configuracion.INSTANCE.getPropiedad("qr.ancho"));
        alto = Integer.parseInt(Configuracion.INSTANCE.getPropiedad("qr.alto"));
        url = Configuracion.INSTANCE.getPropiedad("qr.url");

    }

    public String getRutaArchivo() throws Exception {
        if (rutaArchivo != null && !rutaArchivo.trim().isEmpty()) {
            return rutaArchivo;
        } else {
            return new GeneradorDeCodigoQR().generarCodigoQR(url + "/" + mascota.getId(), alto, ancho, "QR-" + mascota.getId());
        }
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    private String rutaArchivo;
    private Mascota mascota;
}
