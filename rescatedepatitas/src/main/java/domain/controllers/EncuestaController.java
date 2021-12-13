package domain.controllers;

//import com.sun.org.apache.xpath.internal.operations.Mod;
import db.EntityManagerHelper;
import domain.entities.Organizacion.Organizacion;
import domain.entities.mascota.Caracteristica;
import domain.entities.personas.Voluntario;
import domain.entities.publicacion.*;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncuestaController {

    private Repositorio<Encuesta> repositorio;
    private Repositorio<Voluntario> repoVoluntario;
    private Repositorio<PreguntaMultiple> repoPreguntasMultiples;
    private Repositorio<RespuestaPosible> repoRespuestaPosible;

    public EncuestaController(){
        this.repositorio = FactoryRepositorio.get(Encuesta.class);
        this.repoVoluntario = FactoryRepositorio.get(Voluntario.class);
        this.repoPreguntasMultiples = FactoryRepositorio.get(PreguntaMultiple.class);
        this.repoRespuestaPosible = FactoryRepositorio.get(RespuestaPosible.class);
    }

    public ModelAndView mostrarEncuentas(Request request, Response response) {
        Long idVoluntario = request.session().attribute("idUsuario");
        Voluntario voluntario = this.repoVoluntario.buscar(idVoluntario);
        Organizacion organizacion = voluntario.getOrganizacion();
        Encuesta encuestaActiva = organizacion.getEncuestaActiva();
        Map<String, Object> parametros = new HashMap<>();
        List<Encuesta> encuestas = organizacion.getEncuestas();
        parametros.put("encuestas", encuestas);
        parametros.put("encuestaActiva", encuestaActiva);
        return new ModelAndView(parametros, "encuestas.hbs");
    }

    public ModelAndView guardarEncuesta(Request request, Response response){
        Long idVoluntario = request.session().attribute("idUsuario");
        Voluntario voluntario = this.repoVoluntario.buscar(idVoluntario);
        Organizacion organizacion = voluntario.getOrganizacion();
        Encuesta encuesta = new Encuesta();
        encuesta.setDescripcion(request.queryParams("descripcionEncuesta"));
        organizacion.agregarEncuesta(encuesta);
        repositorio.agregar(encuesta);
        response.redirect("/encuesta");
        return null;
    }

    public ModelAndView setEncuestaActiva(Request request, Response response){
        Long idVoluntario = request.session().attribute("idUsuario");
        Voluntario voluntario = this.repoVoluntario.buscar(idVoluntario);
        Organizacion organizacion = voluntario.getOrganizacion();
        Integer idEncuesta = Integer.parseInt(request.queryParams("encuestaId"));
        Encuesta encuesta = this.repositorio.buscar(idEncuesta);
        organizacion.setEncuestaActiva(encuesta);
        repositorio.modificar(organizacion);
        response.redirect("/encuesta");
        return null;
    }

    public Response agregarRespuesta(Request request, Response response) {
        Long id = Long.parseLong(request.queryParams("id"));

        if(id == -1){
            /*
            RespuestaPosible respuestaPosible = new RespuestaPosible();
            respuestaPosible.setRespuesta(request.queryParams("descripcion"));
            this.repo.agregar(caracteristicasMascota);
            */
        } else {
            PreguntaMultiple preguntaMultiple = this.repoPreguntasMultiples.buscar(id);
            RespuestaPosible respuestaPosible = new RespuestaPosible();
            respuestaPosible.setRespuesta(request.queryParams("descripcion"));
            preguntaMultiple.agregarRespuesta(respuestaPosible);

            this.repoPreguntasMultiples.modificar(preguntaMultiple);
            response.redirect("/encuesta");
            return response;
        }

        response.redirect("/encuesta");
        return response;
    }
}
