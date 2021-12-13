package domain.entities.formaDeNotificacion;

import ExcepcionesPersonalizadas.ExcepcionMensajeMail;
import configuracion.Configuracion;
import domain.entities.otros.MensajeMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AdapterAPIJavaMail implements AdapterNotificacionPorEmail{

    private static Message prepareMessage(Session session, String myAccountEmail, String mensaje, String destinatario) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(Configuracion.INSTANCE.getPropiedad("javaMail.SUBJECT"));
            String htmlCode = mensaje;
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(AdapterAPIJavaMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void mandarEmail(String contacto, String mensaje) {
        System.out.println("Preparando para mandar el email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", Configuracion.INSTANCE.getPropiedad("javaMail.AUTH"));
        properties.put("mail.smtp.starttls.enable", Configuracion.INSTANCE.getPropiedad("javaMail.STARTTLS"));
        properties.put("mail.smtp.host", Configuracion.INSTANCE.getPropiedad("javaMail.HOST"));
        properties.put("mail.smtp.port", Configuracion.INSTANCE.getPropiedad("javaMail.PORT"));

        String myAccountEmail = Configuracion.INSTANCE.getPropiedad("javaMail.MAIL");
        String password = Configuracion.INSTANCE.getPropiedad("javaMail.PASSWORD");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        Message message = prepareMessage(session, myAccountEmail, mensaje, contacto);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ExcepcionMensajeMail();
        }
        System.out.println("El mail se envio correctamente");
    }
}
