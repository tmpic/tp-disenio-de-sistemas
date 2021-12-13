package domain.controllers;

import com.google.gson.JsonObject;
import domain.entities.formaDeNotificacion.NotificadorPorMail;
import domain.entities.formaDeNotificacion.NotificadorPorSMS;
import domain.entities.formaDeNotificacion.NotificadorPorWhatsApp;
import domain.entities.mascota.Mascota;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Duenio;
import domain.entities.personas.Persona;
import domain.entities.personas.Rescatista;
import domain.entities.publicacion.Publicacion;
import domain.entities.publicacion.PublicacionDarEnAdopcion;
import domain.entities.publicacion.PublicacionDeMascotaEncontrada;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.halt;

public class UsuarioController {

    private Repositorio<Duenio> repositorio;
    private Repositorio<Mascota> repoMascotas;
    private Repositorio<Rescatista> repositorioRescatistas;
    private Repositorio<DatosDeContacto> repoDatosDeContacto;

    public UsuarioController() {
        this.repositorio = FactoryRepositorio.get(Duenio.class);
        this.repoMascotas = FactoryRepositorio.get(Mascota.class);
        this.repositorioRescatistas = FactoryRepositorio.get(Rescatista.class);
        this.repoDatosDeContacto = FactoryRepositorio.get(DatosDeContacto.class);
    }

    public ModelAndView mostrar(Request request, Response response){
        String idUsuario = request.session().attribute("idUsuario").toString();
        Duenio usuario = this.repositorio.buscar(new Long(idUsuario));
        Rescatista rescatista = this.repositorioRescatistas.buscar(new Long(idUsuario));
        Map<String, Object> parametros = new HashMap<>();
        List<Mascota> lista_mascotas = this.repoMascotas.buscarTodos();
        if(usuario != null){
            parametros.put("usuario", usuario);
            List<Mascota> mascotas = lista_mascotas.stream().filter((unaMascota) -> unaMascota.getDuenio().getId() == usuario.getId()).collect(Collectors.toList());
            parametros.put("mascota", mascotas);
            return new ModelAndView(parametros, "user.hbs");
        } else if(rescatista != null){
            parametros.put("usuario", rescatista);
            List<Mascota> mascotas = lista_mascotas.stream().filter((unaMascota) -> unaMascota.getDuenio().getId() == rescatista.getId()).collect(Collectors.toList());
            parametros.put("mascota", mascotas);
            return new ModelAndView(parametros, "user.hbs");
        } else{
            halt(401, "Acceso denegado");
        }

        return new ModelAndView(null, "user.hbs");
    }

    public Response guardarDatosDeContacto(Request request, Response response) throws UnsupportedEncodingException {
        Duenio duenio = repositorio.buscar(request.session().attribute("idUsuario"));
        List<DatosDeContacto> datosDeContactos = duenio.getDatosDeContactos();
        List<Long> ids_vm = new ArrayList<Long>();

        String[] datitos = request.queryParamsValues("datitos[]");
        for (String datito:datitos) {
            String myStringDecoded = URLDecoder.decode(datito, "UTF-8");

            String[] parts = myStringDecoded.split("&");
            JsonObject json = new JsonObject();

            for (String part : parts) {
                String[] keyVal = part.split("=");
                json.addProperty(keyVal[0], keyVal[1]);
            }

            DatosDeContacto contacto = new DatosDeContacto();
            Long id_dato = json.get("id") == null ? null : json.get("id").getAsLong();
            if(id_dato != null){
                contacto = datosDeContactos.stream().filter(d -> d.getId().equals(id_dato)).findFirst().get();
                ids_vm.add(id_dato);
            }
            else{
                datosDeContactos.add(contacto);
            }

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
        }

        List<DatosDeContacto> datosAEliminar = datosDeContactos.stream().filter(d -> !ids_vm.contains(d.getId()) && d.getId() != null).collect(Collectors.toList());
        for (DatosDeContacto dato:datosAEliminar) {
            datosDeContactos.remove(dato);
            repoDatosDeContacto.eliminar(dato);
        }

        duenio.setDatosDeContactos(datosDeContactos);
        repositorio.modificar(duenio);

        response.redirect("/miPerfil");
        return response;
    }
}
