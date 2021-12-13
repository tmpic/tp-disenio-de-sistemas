package domain.entities.formaDeNotificacion;

import domain.entities.personas.DatosDeContacto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("whatsapp")
public class NotificadorPorWhatsApp extends FormaDeNotificacion{
    @Transient
    private AdapterNotificacionPorWhatsApp notificadorPorWhatsApp;

    public NotificadorPorWhatsApp(){
        notificadorPorWhatsApp = new AdapterAPITwilipWhatsApp();
    }
    public void contactarDuenio(DatosDeContacto contacto, String mensaje) {
        notificadorPorWhatsApp.mandarWhatsApp(contacto.getTelefono(),mensaje);
    }
}