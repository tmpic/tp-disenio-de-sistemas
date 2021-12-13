package domain.entities.formaDeNotificacion;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import configuracion.Configuracion;

public class AdapterAPITwilipWhatsApp implements AdapterNotificacionPorWhatsApp{
    private String ACCOUNT_SID;
    private String AUTH_TOKEN;
    private String WPPEMISOR;

    public AdapterAPITwilipWhatsApp() {
        ACCOUNT_SID = Configuracion.INSTANCE.getPropiedad("twilio.ACCOUNT_SID");
        AUTH_TOKEN = Configuracion.INSTANCE.getPropiedad("twilio.AUTH_TOKEN");
        WPPEMISOR = Configuracion.INSTANCE.getPropiedad("wp.emisor");
    }

    public void mandarWhatsApp(String numero, String mensaje) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+numero),
                new com.twilio.type.PhoneNumber(WPPEMISOR),
                mensaje)
                .create();
    }
}
