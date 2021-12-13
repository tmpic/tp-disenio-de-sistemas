package domain.entities.formaDeNotificacion;

import javax.mail.MessagingException;

public interface AdapterNotificacionPorEmail {
    public void mandarEmail(String contacto,String mensaje) throws MessagingException;
}
