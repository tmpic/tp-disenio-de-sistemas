package domain.entities.hogar.FiltrosHogares;
import domain.entities.hogar.Hogar;
import domain.entities.hogar.Ubicacion;
import domain.entities.mascota.Especie;

import java.util.Collection;

public class FiltrosHogares {

    public  boolean filtros(Hogar hogar, Ubicacion ubicacionRescatista, Especie especie, Boolean patio, float radio, Collection<String> caracteristicas) {

       return disponibilidad(hogar) && TienePatio(hogar) && AmenosDe(hogar,ubicacionRescatista, radio) && AceptaCaracteristicas(hogar,caracteristicas) && EspecieEspecifica(hogar,especie);
    }

    public  boolean disponibilidad(Hogar hogar){
        return hogar.lugares_disponibles > 0;
    }

    public  boolean TienePatio(Hogar hogar){
        return hogar.patio;
    }

    public  boolean AmenosDe(Hogar hogar,Ubicacion ubiRescatista,float distancia){
        return distancia < 0 || hogar.ubicacion.distanciaCon(ubiRescatista) >= distancia;
    }
    public  boolean AceptaCaracteristicas(Hogar hogar, Collection<String> listaComodidades){
        return listaComodidades == null || hogar.caracteristicas.containsAll(listaComodidades);
    }
    public  boolean EspecieEspecifica(Hogar hogar, Especie especie){
        return especie == null || especie.getTipo().equalsIgnoreCase("Perro") ? hogar.admisiones.perros : hogar.admisiones.gatos;
    }

}

