package domain.entities.formaDeNotificacion;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import configuracion.Configuracion;

public class AdapterAPITwilioSMS implements AdapterNotificacionPorSMS {
    private String ACCOUNT_SID;
    private String AUTH_TOKEN;
    private String MESSAGING_ID;

    public AdapterAPITwilioSMS() {
        ACCOUNT_SID = Configuracion.INSTANCE.getPropiedad("twilio.ACCOUNT_SID");
        AUTH_TOKEN = Configuracion.INSTANCE.getPropiedad("twilio.AUTH_TOKEN");
        MESSAGING_ID = Configuracion.INSTANCE.getPropiedad("sms.MENSAJE_SID");
    }

    public void mandarSMS(String numero, String mensaje) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(numero),
                new com.twilio.type.PhoneNumber(MESSAGING_ID),
                mensaje)
                .create();
    }
}
