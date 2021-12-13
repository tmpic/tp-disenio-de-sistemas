package domain.controllers;


import com.google.gson.JsonObject;
import domain.entities.formaDeNotificacion.NotificadorPorMail;
import domain.entities.formaDeNotificacion.NotificadorPorSMS;
import domain.entities.formaDeNotificacion.NotificadorPorWhatsApp;
import domain.entities.otros.TipoDeDocumento;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Duenio;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import retrofit2.http.QueryMap;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import validacionDeClave.ValidacionDeClave;

import javax.jws.WebParam;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class RegisterController {
    private Repositorio<Duenio> repositorio;


    public RegisterController(){
        this.repositorio = FactoryRepositorio.get(Duenio.class);
    }



    private Duenio asingarParametrosDuenio(Duenio duenio,Request request) throws UnsupportedEncodingException {


        duenio.setNombreDeUsuario(request.queryParams("user"));
        duenio.setPassword(request.queryParams("pass"));
        duenio.setNombreYApellido(request.queryParams("nYA"));

        String fecha = request.queryParams("nac");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        duenio.setFechaDeNacimiento(date);
        duenio.setTipoDocumento(TipoDeDocumento.valueOf(request.queryParams("tipoDocumento")));
        duenio.setNumeroDocumento(new Integer(request.queryParams("dni")));

        String[] datitos = request.queryParamsValues("datitos[]");
        for (String datito:datitos) {
            String myStringDecoded = URLDecoder.decode(datito, "UTF-8");

            String[] parts = myStringDecoded.split("&");
            JsonObject json = new JsonObject();

            for (String part : parts) {
                String[] keyVal = part.split("="); // The equal separates key and values
                System.out.println(keyVal[0] + " - " + keyVal[1] );
                json.addProperty(keyVal[0], keyVal[1]);
            }

            DatosDeContacto contacto = new DatosDeContacto();
            contacto.setNombreYApellido(json.get("contName").getAsString());
            contacto.setEmail(json.get("contMail").getAsString());
            contacto.setTelefono(json.get("contTel").getAsString());

            if (json.get("whatsapp").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorWhatsApp());
            }
            if (json.get("mail").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorMail());
            }
            if (json.get("sms").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorSMS());
            }

            duenio.agregarDatosDeContacto(contacto);
        }

        return duenio;

    }

    public Object crearUsuario(Request request, Response response) throws IOException, ServletException {
        response.type("application/json");
        Duenio duenio = new Duenio();
        DatosDeContacto datoContacto = new DatosDeContacto();
        String userName = request.queryParams("user");
        String password = request.queryParams("pass");;
        List<Duenio> duenios = repositorio.buscarTodos();
        if(duenios.stream().anyMatch(d -> d.getNombreDeUsuario().equals(userName))) {
            return "{\"exito\":false, \"mensajeError\":\"El nombre de usuario ya existe.\"}";
        }
        try {
            new ValidacionDeClave().validarClave(password);
        }catch(Exception ex) {
            return "{\"exito\":false, \"mensajeError\":\"" + ex.getMessage() + "\"}";
        }
        duenio = this.asingarParametrosDuenio(duenio, request);
        this.repositorio.agregar(duenio);


        return "{\"exito\":true}";
    }


    public ModelAndView vistaRegistro(Request request, Response response){
        return new ModelAndView(null,"Register.hbs");
    }
}

