package domain.entities.hogar;

import domain.entities.hogar.FiltrosHogares.FiltrosHogares;
import domain.entities.mascota.Especie;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BuscadorDeHogar {
    public List<Hogar> buscar(Ubicacion ubicacion, Especie especie, Boolean patio, Float radio, Collection<String> caracteristicas) throws IOException {
        ServicioHogares servicioHogares = domain.entities.hogar.ServicioHogares.instancia();
        FiltrosHogares filtrosHogares = new FiltrosHogares();

        int total, i = 1;
        List<Hogar> hogares =  new ArrayList();
        do {
            HogarDTO hogarDTO = servicioHogares.listadoDeHogares(i);
            total = hogarDTO.total;


            hogares.addAll(hogarDTO.hogares.stream().collect(Collectors.toList()));
            
            hogares.stream().filter(hogar -> hogar.patio);
        } while (total < i * 10);
/*
*
*             hogares.addAll(hogarDTO.hogares.stream().filter(hogar ->
                    FiltrosHogares.filtros(hogar,ubicacion,especie,patio,radio,caracteristicas)
            ).collect(Collectors.toList()));
            * */

        return hogares;
    }

    public List<Hogar> obtenerTodos() throws IOException {
        ServicioHogares servicioHogares = domain.entities.hogar.ServicioHogares.instancia();



        List<Hogar> hogares =  new ArrayList();
        for (int j =1; j<5; j++ ){

            HogarDTO hogarDTO = servicioHogares.listadoDeHogares(j);

            hogares.addAll(hogarDTO.hogares.stream().collect(Collectors.toList()));
        }


        return hogares;
    }
}