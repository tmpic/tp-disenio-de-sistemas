package domain.entities.formaDeNotificacion;

import ExcepcionesPersonalizadas.ExcepcionMensajeMail;
import domain.entities.personas.DatosDeContacto;

import javax.mail.MessagingException;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("mail")
public class NotificadorPorMail extends FormaDeNotificacion{
    @Transient
    private AdapterNotificacionPorEmail adapterNotificacionPorEmail;

    public NotificadorPorMail(){
        this.adapterNotificacionPorEmail = new AdapterAPIJavaMail();
    }

    public void contactarDuenio(DatosDeContacto datos, String mensaje) {
        try {
            this.adapterNotificacionPorEmail.mandarEmail(datos.getEmail(),mensaje);
        } catch (MessagingException e) {
            throw new ExcepcionMensajeMail();
        }
    }
}
