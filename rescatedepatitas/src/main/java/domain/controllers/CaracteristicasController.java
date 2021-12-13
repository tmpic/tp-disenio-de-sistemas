package domain.controllers;

import domain.entities.mascota.Caracteristica;
import domain.entities.mascota.Caracteristica;
import domain.entities.mascota.CaracteristicasMascotas;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import server.Router;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaracteristicasController {
    private Repositorio<Caracteristica> repo;
    private Repositorio<CaracteristicasMascotas> repositorioCaracteristicasMascotas;

    public CaracteristicasController() {
        this.repo = FactoryRepositorio.get(Caracteristica.class);
        this.repositorioCaracteristicasMascotas = FactoryRepositorio.get(CaracteristicasMascotas.class);
    }

    public ModelAndView mostrarTodos(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();
        List<Caracteristica> caracteristica = this.repo.buscarTodos();
        parametros.put("caracteristica", caracteristica);
        return new ModelAndView(parametros, "caracteristicas.hbs");
    }

    public ModelAndView mostrar(Request request, Response response){
        Caracteristica caracteristica = this.repo.buscar(new Integer(request.params("id")));
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("caracteristica", caracteristica);

        return new ModelAndView(parametros, "caracteristicas.hbs");
    }

    public Response eliminar(Request request, Response response){
        Long numero = new Long(request.params("id"));
        List<CaracteristicasMascotas> caracteristicasMascotas = this.repositorioCaracteristicasMascotas.buscarTodos().stream().filter(car -> car.getCaracteristica().getId().equals(numero)).collect(Collectors.toList());
        caracteristicasMascotas.forEach(carFiltrada -> this.repositorioCaracteristicasMascotas.eliminar(carFiltrada));//TODO probablemente haya que hacer lo mismo con las publicaciones FK
        Caracteristica caracteristica = this.repo.buscar(numero);
        this.repo.eliminar(caracteristica);
        response.redirect("/caracteristicas", 200);
        return response;
    }

    public Response guardar(Request request, Response response) {
        Long id = Long.parseLong(request.queryParams("id"));
        if(id == -1){
            Caracteristica caracteristicasMascota = new Caracteristica();
            caracteristicasMascota.setUnaCaracteristica(request.queryParams("descripcion"));
            this.repo.agregar(caracteristicasMascota);
        } else {
            Caracteristica caracteristicasMascota = this.repo.buscar(id);
            caracteristicasMascota.setUnaCaracteristica(request.queryParams("descripcion"));
            this.repo.modificar(caracteristicasMascota);
            response.redirect("/caracteristicas");
            return response;
        }

        response.redirect("/caracteristicas");
        return response;
    }

}
