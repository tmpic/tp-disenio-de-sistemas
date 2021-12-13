package domain.controllers;

import com.google.gson.JsonObject;
import configuracion.Configuracion;
import domain.entities.Organizacion.Organizacion;
import domain.entities.ResizerDeImagenes.ResizerDeImagen;
import domain.entities.formaDeNotificacion.NotificadorPorMail;
import domain.entities.formaDeNotificacion.NotificadorPorSMS;
import domain.entities.formaDeNotificacion.NotificadorPorWhatsApp;
import domain.entities.mascota.*;
import domain.entities.otros.Foto;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Duenio;
import domain.entities.personas.Persona;
import domain.entities.publicacion.Encuesta;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.hibernate.bytecode.spi.NotInstrumentedException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class MascotaController {


    private Repositorio<Mascota> repositorio;
    private Repositorio<Especie> repoEspecie;
    private Repositorio<Raza> repoRaza;
    private Repositorio<Duenio> repoDuenio;
    private Repositorio<Caracteristica> repoCaracteristica;

    private Repositorio<Organizacion> repositorioOrganizaciones;
    private Repositorio<Encuesta> repoEncuestas;

    public MascotaController() {
        this.repositorio = FactoryRepositorio.get(Mascota.class);
        this.repoEspecie = FactoryRepositorio.get(Especie.class);
        this.repoRaza = FactoryRepositorio.get(Raza.class);
        this.repoDuenio = FactoryRepositorio.get(Duenio.class);
        this.repoCaracteristica = FactoryRepositorio.get(Caracteristica.class);

        this.repoEncuestas = FactoryRepositorio.get(Encuesta.class);
        this.repositorioOrganizaciones = FactoryRepositorio.get(Organizacion.class);
    }

    public ModelAndView perfilMascota(Request request, Response response) {
        //throw new NotInstrumentedException("");
        Long id_duenio = new Long(request.session().attribute("idUsuario").toString());

        Long id = new Long(0);
        try {
            id = Long.parseLong(request.params("idMascota"));
        } catch (Exception e) {
            response.status(400);
            halt(400, "El id de la mascota no es valido");
        }

        Mascota mascota = this.repositorio.buscar(id);

        if (mascota == null) {
            response.status(404);
            halt(400, "La mascota no existe");
        }else if(!mascota.getDuenio().getId().equals(id_duenio)){
            response.status(404);
            halt(400, "Esa mascota no le pertenece");
        }

        return new ModelAndView(mascota, "Perfil_Mascota_Perdida.hbs");
    }

    public List<Especie> getEspecies() {
        List<Especie> especies = this.repoEspecie.buscarTodos();
        return especies;
    }

    public ModelAndView registroMascota(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("caracteristicas", repoCaracteristica.buscarTodos());
        parametros.put("especies", repoEspecie.buscarTodos());
        parametros.put("organizaciones", repositorioOrganizaciones.buscarTodos());
        return new ModelAndView(parametros, "registrarMascota.hbs");
    }

    public Response crearMascota(Request request, Response response) throws ServletException, IOException {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        Mascota mascota = new Mascota();
        mascota = this.asignarParametrosMascota(mascota, request);

        Collection<Part> files = request.raw().getParts().stream().filter(file -> file.getName().equals("fotos")).collect(Collectors.toList());

        for (Part file : files) {
            String GUIDwithext = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            String ruta = "target/classes/public/img/" + GUIDwithext;
            String rutaRelativa = "img\\" + GUIDwithext;
            File targetFile = new File(ruta);

            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);

            ResizerDeImagen resizer = new ResizerDeImagen();
            resizer.normalizarImagen(ruta);


            mascota.agregarFotos(new Foto(rutaRelativa));
        }

        this.repositorio.agregar(mascota);
        response.redirect("/inicio");
        return response;
    }

    private Mascota asignarParametrosMascota(Mascota mascota, Request request) throws ServletException, IOException {
        mascota.setNombre(getValue(request.raw().getPart("nombreMascota")));
        mascota.setApodo(getValue(request.raw().getPart("apodo")));
        mascota.setEdad(Integer.valueOf(getValue(request.raw().getPart("edad"))));
        mascota.setDescripcionFisica(getValue(request.raw().getPart("descripcion")));
        mascota.setSexo(getValue(request.raw().getPart("sexo")));
        mascota.setEspecie(this.repoEspecie.buscar(Long.parseLong(getValue(request.raw().getPart("especie")))));
        mascota.setRaza(this.repoRaza.buscar(Long.parseLong(getValue(request.raw().getPart("raza")))));
        mascota.setEstado(EstadoMascota.ENCONTRADA);
        mascota.setOrganizacion(this.repositorioOrganizaciones.buscar(Integer.parseInt(getValue(request.raw().getPart("organizacion")))));
        Long id_duenio = new Long(request.session().attribute("idUsuario").toString());
        mascota.setDuenio(repoDuenio.buscar(id_duenio));

        String[] caracteristicas = request.queryParamsValues("caracteristicas[]");
        if (caracteristicas != null)
            for (String caracteristica : caracteristicas) {
                String myStringDecoded = URLDecoder.decode(caracteristica, "UTF-8");

                String[] parts = myStringDecoded.split("&");
                JsonObject json = new JsonObject();

                for (String part : parts) {
                    String[] keyVal = part.split("="); // The equal separates key and values
                    System.out.println(keyVal[0] + " - " + keyVal[1]);
                    json.addProperty(keyVal[0], keyVal[1]);
                }

                CaracteristicasMascotas caracteristicasMascotas = new CaracteristicasMascotas();
                caracteristicasMascotas.setDescripcion(json.get("valor").getAsString());
                caracteristicasMascotas.setCaracteristica(repoCaracteristica.buscar(json.get("idCaracteristica").getAsLong()));

                mascota.agregarCaracteristica(caracteristicasMascotas);
            }


        return mascota;
    }

    private static String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0; ) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }

    public ModelAndView mostrarFormularioParaDarEnAdopcion(Request request, Response response){
        Long idUsuario = request.session().attribute("idUsuario");
        List<Mascota> mascotas = repositorio.buscarTodos();
        mascotas.removeIf(mascota -> mascota.getDuenio().getId() != idUsuario);
        Map<String, Object> parametros = new HashMap<>();
        try {
            if (!request.queryParams("mascota").isEmpty()) {
                Long idMascota = Long.parseLong(request.queryParams("mascota"));
                Mascota mascotaSeleccionada = repositorio.buscar(idMascota);
                if(mascotaSeleccionada.getDuenio().getId() != idUsuario){
                    response.redirect("/");
                }
                Organizacion organizacion = mascotaSeleccionada.getOrganizacion();
                Encuesta encuestaBasica = repoEncuestas.buscar(0);
                Encuesta encuestaActiva = organizacion.getEncuestaActiva();
                Encuesta encuestaFinal = new Encuesta();
                encuestaFinal.setDescripcion("Esta es la encuesta basica + encuesta activa");
                encuestaFinal.agregarPreguntas(encuestaBasica.getPreguntas());
                encuestaFinal.agregarPreguntas(encuestaActiva.getPreguntas());
                parametros.put("encuesta", encuestaFinal);
                parametros.put("mascotaSeleccionada", mascotaSeleccionada);
            }
        } catch (NullPointerException e) {
        }
        //TODO Sacar la organizacion de la mascota
        parametros.put("mascotas",mascotas);
        return new ModelAndView(parametros, "Reg_Masc_Dar_Adop.hbs");
    }
}
