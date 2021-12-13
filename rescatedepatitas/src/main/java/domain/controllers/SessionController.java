package domain.controllers;

import spark.Request;
import spark.Response;

public class SessionController {

    public void validarUsuario(Request request, Response response, String tipo) {
        boolean autenticado = request.session().attribute("idUsuario") != null;
        String tipoDeUsuario = request.session().attribute("tipoUsuario");
        if (!autenticado || tipoDeUsuario != tipo) {
            response.redirect("/login" + tipo);
        }
    }
}
