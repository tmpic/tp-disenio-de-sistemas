package domain.controllers;

import domain.entities.mascota.Caracteristica;
import domain.entities.mascota.CaracteristicasMascotas;
import domain.entities.mascota.Especie;
import domain.entities.mascota.Raza;
import domain.entities.personas.Duenio;
import domain.entities.publicacion.PublicacionPedirAdopcion;

import domain.repositories.Repositorio;
import org.omg.CORBA.WStringSeqHelper;
import spark.Request;
import spark.Response;

import java.util.List;

public class IntencionAdopcionController {
    private Repositorio<Duenio> repositorio;



    public Response generarPublicacionAdopcion(Request request, Response response) {


        List<CaracteristicasMascotas> preferencias = null;
        Boolean patioBool = false;

        String patio = request.queryParams("patioValue");
        String tamanio = request.queryParams("tamanio");
        String otrasMascotas = request.queryParams("mascotasValue");
        String juguetes = request.queryParams("JuguetesValue");
        String paseo = request.queryParams("paseoValue");
        String pelaje = request.queryParams("PelajeValue");
        String energia = request.queryParams("energiaMascota");
        String obediencia = request.queryParams("obedienciaValue");
        String sexo = request.queryParams("sexoValue");
        String especie = request.queryParams("especie");
        String raza = request.queryParams("raza");

        if(patio == "Si"){
            patioBool = true;
        }

        CaracteristicasMascotas tamanioMascota = new CaracteristicasMascotas();
        tamanioMascota.setDescripcion(tamanio);
        CaracteristicasMascotas mascotas = new CaracteristicasMascotas();
        mascotas.setDescripcion(otrasMascotas);
        CaracteristicasMascotas tieneJuguetes = new CaracteristicasMascotas();
        tieneJuguetes.setDescripcion(juguetes);
        CaracteristicasMascotas paseos = new CaracteristicasMascotas();
        paseos.setDescripcion(paseo);
        CaracteristicasMascotas tipoPelaje = new CaracteristicasMascotas();
        tipoPelaje.setDescripcion(pelaje);
        CaracteristicasMascotas cantidadEnergia = new CaracteristicasMascotas();
        cantidadEnergia.setDescripcion(energia);
        CaracteristicasMascotas nivelObediencia = new CaracteristicasMascotas();
        nivelObediencia.setDescripcion(obediencia);
        Especie especieMascota = new Especie();
        especieMascota.setTipo(especie);
        Raza razaMascota = new Raza();
        razaMascota.setNombre(raza);

        Duenio futuro_duenio = repositorio.buscar(new Long(request.session().attribute("idUsuario").toString()));



        preferencias.add(tamanioMascota);
        preferencias.add(mascotas);
        preferencias.add(tieneJuguetes);
        preferencias.add(paseos);
        preferencias.add(tipoPelaje);
        preferencias.add(cantidadEnergia);
        preferencias.add(nivelObediencia);


        PublicacionPedirAdopcion publicacion = new PublicacionPedirAdopcion(futuro_duenio,preferencias,patioBool,null,especieMascota,razaMascota,sexo,null);


        //repositorio.agregar(publicacion);

        response.redirect("/");

        return response;
    }
    }

