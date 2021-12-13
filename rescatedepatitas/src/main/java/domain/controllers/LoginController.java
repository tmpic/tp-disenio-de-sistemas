package domain.controllers;

import com.google.common.hash.Hashing;
import domain.entities.personas.Administrador;
import domain.entities.personas.Duenio;
import domain.entities.personas.Voluntario;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.nio.charset.Charset;
import java.util.*;

import static spark.Spark.halt;


public class LoginController {
    private Repositorio<Duenio> repositorio;
    private Repositorio<Administrador> repositorioAdmins;
    private Repositorio<Voluntario> repositorioVoluntarios;

    public LoginController() {
        this.repositorio = FactoryRepositorio.get(Duenio.class);
        this.repositorioAdmins = FactoryRepositorio.get(Administrador.class);
        this.repositorioVoluntarios = FactoryRepositorio.get(Voluntario.class);
    }

    public ModelAndView loginUsuario(Request request, Response response, String error) {
        String tipo = "duenio";
        HashMap<String, String> tipasos = new HashMap<String, String>();
        tipasos.put("tipo", tipo);
        tipasos.put("action", "/loginDuenio");
        if (error != null)
            tipasos.put("error", error);
        return new ModelAndView(tipasos, "Login.hbs");
    }

    public ModelAndView loginAdmin(Request request, Response response, String error) {
        String tipo = "administrador";
        HashMap<String, String> tipasos = new HashMap<String, String>();
        tipasos.put("tipo", tipo);
        tipasos.put("action", "/loginAdmin");
        if (error != null)
            tipasos.put("error", error);
        return new ModelAndView(tipasos, "Login.hbs");
    }

    public ModelAndView loginVoluntario(Request request, Response response, String error) {
        String tipo = "voluntario";
        HashMap<String, String> tipasos = new HashMap<String, String>();
        tipasos.put("tipo", tipo);
        tipasos.put("action", "/loginVoluntario");
        if (error != null)
            tipasos.put("error", error);
        return new ModelAndView(tipasos, "Login.hbs");
    }

    public ModelAndView autenticarVoluntario(Request request, Response response) {
        List<Voluntario> voluntarios = this.repositorioVoluntarios.buscarTodos();
        String usuario = request.queryParams("usuario");
        String pass = request.queryParams("password");

        String passHasheada = Hashing.md5().newHasher()
                .putString(pass, Charset.defaultCharset())
                .hash()
                .toString();


        Voluntario voluntario = voluntarios.stream().filter(v -> usuario.equals(v.getNombreYApellido())).findFirst().orElse(null);

        if (voluntario == null) {
            return loginVoluntario(request, response, "Usuario o contraseña incorrecto.");
        } else {
            if (!(voluntario.getPassword().equals(passHasheada))) {
                return loginVoluntario(request, response, "Usuario o contraseña incorrecto.");
            }
            request.session().attribute("idUsuario", voluntario.getId());
            request.session().attribute("tipoUsuario", "Voluntario");
        }
        response.redirect("/publicaciones/pendientes");
        return new PublicacionesController().mostrarPendientes(request, response);
    }

    public ModelAndView autenticarAdmin(Request request, Response response) {
        List<Administrador> admins = this.repositorioAdmins.buscarTodos();
        String usuario = request.queryParams("usuario");
        String pass = request.queryParams("password");

        String passHasheada = Hashing.md5().newHasher()
                .putString(pass, Charset.defaultCharset())
                .hash()
                .toString();


        Administrador administrador = admins.stream().filter(admin -> usuario.equals(admin.getNombreDeUsuario())).findFirst().orElse(null);

        if (administrador == null) {
            return loginAdmin(request, response, "Usuario o contraseña incorrecto.");
        } else {
            if (!(administrador.getPassword().equals(passHasheada))) {
                return loginAdmin(request, response, "Usuario o contraseña incorrecto.");
            }
            request.session().attribute("idUsuario", administrador.getId());
            request.session().attribute("tipoUsuario", "Admin");
        }
        return new CaracteristicasController().mostrarTodos(request, response);
    }

    public ModelAndView autenticarUsuario(Request request, Response response) {
        List<Duenio> duenios = this.repositorio.buscarTodos();
        String usuario = request.queryParams("usuario");
        String pass = request.queryParams("password");

        String passHasheada = Hashing.md5().newHasher()
                .putString(pass, Charset.defaultCharset())
                .hash()
                .toString();


        Duenio due = duenios.stream().filter(duenio -> usuario.equals(duenio.getNombreDeUsuario())).findFirst().orElse(null);

        if (due == null) {
            return loginUsuario(request, response, "Usuario o contraseña incorrecto.");
        } else {
            if (!(due.getPassword().equals(passHasheada))) {

                return loginUsuario(request, response, "Usuario o contraseña incorrecto.");
            }
            request.session().attribute("idUsuario", due.getId());
            request.session().attribute("tipoUsuario", "Duenio");
        }
        response.redirect("/animalesPerdidos");
        return new PublicacionesController().formularioMascotaPerdida(request, response);
    }

    public Response logout(Request request, Response response) {
        request.session().invalidate();
        response.redirect("/");
        return response;
    }
}

