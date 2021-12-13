package server;

import domain.controllers.*;
import spark.ResponseTransformer;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.*;

import static spark.Spark.*;

public class Router {
    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .withHelper("equals", EqualsHelper.equals)
                .withHelper("for", ForHelper.forHB)
                .withHelper("json", JsonHelper.json)
                .build();
    }

    public static void init() {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Spark.externalStaticFileLocation("target/classes/public");
        Router.configure();
    }

    private static void configure() {
        CaracteristicasController caracteristicasController = new CaracteristicasController();
        PublicacionesController publicacionesController = new PublicacionesController();
        EncuestaController encuestaController = new EncuestaController();
        MascotaController mascotaController = new MascotaController();
        RegisterController registerController = new RegisterController();
        LoginController loginController = new LoginController();
        SessionController sesionController = new SessionController();
        UsuarioController usuarioController = new UsuarioController();
        HogaresController hogaresController = new HogaresController();
        IntencionAdopcionController intencionAdopcionController = new IntencionAdopcionController();
        
        
        Spark.get("/detallePublicacion/:idPublicacion", publicacionesController::detallePublicacion, Router.engine);
        Spark.get("/misPublicaciones", publicacionesController::mostrarMisPublicaciones, Router.engine);
        Spark.delete("/misPublicaciones/:id", publicacionesController::eliminarPublicacion);

        Spark.get("/loginVoluntario",(res, req) -> loginController.loginVoluntario(res, req, null), Router.engine);
        Spark.post("/loginVoluntario", loginController::autenticarVoluntario, Router.engine);

        Spark.get("/loginAdmin", (res,req) -> loginController.loginAdmin(res,req, null), Router.engine);
        Spark.post("/loginAdmin", loginController::autenticarAdmin, Router.engine);

        Spark.get("/caracteristicas", caracteristicasController::mostrarTodos, Router.engine);
        Spark.delete("/caracteristicas/:id", caracteristicasController::eliminar);
        Spark.post("/caracteristicas", caracteristicasController::guardar);
        Spark.before("/caracteristicas", (request, response) -> {
            sesionController.validarUsuario(request, response, "Admin");
        });

        Spark.get("/mascotaEncontrada", publicacionesController::formularioMascotaPerdida, Router.engine);
        Spark.post("/animalesPerdidos", "multipart/form-data", publicacionesController::guardar);

        Spark.get("/mascotaEncontrada/:id", publicacionesController::getMascotaEncontrada, Router.engine);
        Spark.post("/mascotaEncontrada/:id", publicacionesController::postMascotaEncontrada);

        Spark.get("/publicacionesAdoptantes", publicacionesController::adoptantePub, Router.engine);

        Spark.get("/crearPublicacionesDeAdoptantes", publicacionesController::crearAdoptantePub, Router.engine);
        Spark.post("/crearPublicacionesDeAdoptantes", publicacionesController::generarPublicacionAdopcion);
        Spark.before("/crearPublicacionesDeAdoptantes", (request, response) -> {
            sesionController.validarUsuario(request, response, "Duenio");
        });

        Spark.get("/adoptarMascota", (request, response) -> publicacionesController.adoptarMascotaPub(request, response, null), Router.engine);
        Spark.get("/animalesPerdidos", publicacionesController::animalesPerdidosPub, Router.engine);
        Spark.get("/", publicacionesController::inicio, Router.engine);

        Spark.get("/inicio", publicacionesController::inicio, Router.engine);

        Spark.get("/busquedaHogares", hogaresController::vistaBusquedaHogares, Router.engine);
        Spark.post("/busquedaHogares", hogaresController::vistaBusquedaHogares, Router.engine);

        Spark.get("/perfilMascota/:idMascota", mascotaController::perfilMascota, Router.engine);
        Spark.before("/registrarMascota", (request, response) -> {
            sesionController.validarUsuario(request, response, "Duenio");
        });
        Spark.get("/registrarMascota", mascotaController::registroMascota, Router.engine);
        Spark.post("/registrarMascota", "multipart/form-data", mascotaController::crearMascota);
        Spark.get("/publicaciones/pendientes", publicacionesController::mostrarPendientes, Router.engine);
        Spark.get("/publicaciones/historial", publicacionesController::mostrarHistorial, Router.engine);
        Spark.before("publicaciones/pendientes", (request, response) -> {
            sesionController.validarUsuario(request, response, "Voluntario");
        });
        Spark.before("publicaciones/historial", (request, response) -> {
            sesionController.validarUsuario(request, response, "Voluntario");
        });

        Spark.get("detallePublicacionDarEnAdopcion/:idPublicacion",publicacionesController :: detallePubDarEnAdopcion,Router.engine);
        Spark.before("/darEnAdopcionMiMascota", (request, response) -> {
            sesionController.validarUsuario(request, response, "Duenio");
        });
        Spark.get("/darEnAdopcionMiMascota", mascotaController::mostrarFormularioParaDarEnAdopcion, Router.engine);
        Spark.post("/darEnAdopcionMiMascota",
                (request, response) -> publicacionesController.generarFormularioParaDarEnAdopcion(request, response));

        Spark.post("/rechazarPublicacion/:id",
                (request, response) -> publicacionesController.rechazarPublicacion(request, response));
        Spark.post("/aceptarPublicacion/:id",
                (request, response) -> publicacionesController.aceptarPublicacion(request, response));

        Spark.get("/encuesta", encuestaController::mostrarEncuentas, Router.engine);
        Spark.before("encuesta", (request, response) -> {
            sesionController.validarUsuario(request, response, "Voluntario");
        });

        Spark.post("/encuesta/nuevaEncuesta",
                (request, response) -> encuestaController.guardarEncuesta(request, response));
        Spark.post("/encuesta/encuestaActiva",
                (request, response) -> encuestaController.setEncuestaActiva(request, response));
        Spark.post("/encuesta/encuestaActiva/respuesta",
                (request, response) -> encuestaController.agregarRespuesta(request, response));

        Spark.get("/register", registerController::vistaRegistro, Router.engine);
        Spark.post("/register", registerController::crearUsuario);

        Spark.post("/loginDuenio", loginController::autenticarUsuario, Router.engine);
        Spark.get("/loginDuenio", (req, res) -> loginController.loginUsuario(req, res, null), Router.engine);

        Spark.get("/logout", loginController::logout);

        Spark.get("miPerfil", usuarioController::mostrar, Router.engine);
        Spark.before("miPerfil", (request, response) -> {
            sesionController.validarUsuario(request, response, "Duenio");
        });

        Spark.before("esMiMascota", (request, response) -> sesionController.validarUsuario(request, response, "Duenio"));
        Spark.post("esMiMascota", (request, response) -> publicacionesController.esMiMascota(request, response));
        Spark.before("quieroAdoptar", (request, response) -> sesionController.validarUsuario(request, response, "Duenio"));
        Spark.post("quieroAdoptar", (request, response) -> publicacionesController.quieroAdoptar(request, response));
        Spark.get("finalizarPublicacion/:token", (request, response) -> publicacionesController.finalizarPublicacion(request, response));

        Spark.post("guardarDatosDeContacto", usuarioController::guardarDatosDeContacto);
        
         Spark.post("AdoptarForm", intencionAdopcionController::generarPublicacionAdopcion);

    }
}
