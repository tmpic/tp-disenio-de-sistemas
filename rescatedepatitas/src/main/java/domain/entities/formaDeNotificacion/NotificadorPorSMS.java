package domain.entities.formaDeNotificacion;

import domain.entities.personas.DatosDeContacto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("sms")
public class NotificadorPorSMS extends  FormaDeNotificacion{
    @Transient
    public AdapterNotificacionPorSMS adapterNotificacionPorSMS;

    public NotificadorPorSMS(){
        adapterNotificacionPorSMS = new AdapterAPITwilioSMS();
    }
    public void contactarDuenio(DatosDeContacto contacto, String mensaje) {
        adapterNotificacionPorSMS.mandarSMS(contacto.getTelefono(),mensaje);
    }
}
