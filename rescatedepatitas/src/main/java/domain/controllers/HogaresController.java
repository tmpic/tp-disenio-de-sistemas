package domain.controllers;

import domain.entities.hogar.BuscadorDeHogar;
import domain.entities.hogar.Hogar;
import domain.entities.hogar.Ubicacion;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.halt;

public class HogaresController {
    public ModelAndView vistaBusquedaHogares(Request request, Response response) throws IOException {

        Map<String, Object> parametros = new HashMap<>();
        String patio = request.queryParams("patio");
        String especie = request.queryParams("especie");
        String disponibilidad = request.queryParams("disponibilidad");
        String comodidad = request.queryParams("comodidad");
        String latitud = request.queryParams("latitud");
        String longitud = request.queryParams("longitud");
        String radio = request.queryParams("radio");

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLat(Double.valueOf(latitud));
        ubicacion.setLongitud(Double.valueOf(longitud));

        List<Hogar> hogares = new BuscadorDeHogar().obtenerTodos();
        hogares.forEach(hogar -> hogar.asignarRadio(ubicacion));

        List<String> comodidades = hogares.stream().flatMap(hogar -> hogar.getCaracteristicas().stream()).collect(Collectors.toList());

        System.out.println(comodidades);
        if (patio != null || especie != null || disponibilidad != null || comodidad != null) {
            if (patio.equals("conPatio")) {
                hogares.removeIf(hogar -> !hogar.isPatio());
            } else if (patio.equals("sinPatio")) {
                hogares.removeIf(hogar -> hogar.isPatio());
            }

            if (especie.equals("gato") || especie.equals("perro") || especie.equals("sinRestriccion")) {
                if (especie.equals("gato")) {

                    hogares.removeIf(hogar -> hogar.getAdmisiones().perros);
                } else if (especie.equals("perro")) {
                    hogares.removeIf(hogar -> hogar.getAdmisiones().gatos);

                } else if (especie.equals(("sinRestriccion"))) {
                    hogares.removeIf(hogar -> !hogar.getAdmisiones().gatos);
                    hogares.removeIf(hogar -> !hogar.getAdmisiones().perros);
                }
            }
            if (disponibilidad != null) {
                hogares.removeIf(hogar -> hogar.lugares_disponibles < 1);
            }

            if (comodidad != null && comodidades.contains(comodidad)) {
                hogares.removeIf(hogar -> !hogar.getCaracteristicas().contains(comodidad));
            }
        }
        if (radio != null && !radio.isEmpty()) {
            hogares.removeIf(hogar -> new Double(String.valueOf(hogar.getRadio())) > (new Double(radio)));
        }
        parametros.put("hogares", hogares);
        parametros.put("comodidades", comodidades);
        parametros.put("latitud", latitud);
        parametros.put("longitud", longitud);

        return new ModelAndView(parametros, "busquedaHogares.hbs");
    }


}
