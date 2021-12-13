package domain.entities.hogar;

import java.util.List;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class Hogar {
    public String id;
    public String nombre;
    public Ubicacion ubicacion;
    public String telefono;
    public Admision admisiones;
    public String tipoAdmision;
    public String tipoDeTamanio;
    public int capacidad;


    public int lugares_disponibles;//HACER ALGO CON LA CAPACIDAD
    public boolean patio;
    public List<String> caracteristicas;
    private boolean mostrar;

    private BigDecimal  radio;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getTipoAdmision() {
        if(admisiones.gatos && admisiones.perros){
            this.tipoAdmision= "sin restriccion";
        }else if(admisiones.perros && !admisiones.gatos){
            this.tipoAdmision ="solo perros";
        }else if(admisiones.gatos && !admisiones.perros ){
            this.tipoAdmision = "solo gatos";
        }
        return this.tipoAdmision;
    }

    public String getTipoDeTamanio(){
        if(this.patio){
            this.tipoDeTamanio = "grandes y medianos";
        }else{
            this.tipoDeTamanio = "pequeños";
        }
        return this.tipoDeTamanio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getLugares_disponibles() {
        return lugares_disponibles;
    }

    public boolean isPatio() {
        return patio;
    }

    public Admision getAdmisiones() {
        return admisiones;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public boolean getMostrar(){
        if(this.caracteristicas.size() > 0){
            this.mostrar = true;
        }else{
            this.mostrar = false;
        }
        return this.mostrar;
    }

    public BigDecimal getRadio() {
        return this.radio;
    }
    public void asignarRadio(Ubicacion ubicacion){
        this.radio = new BigDecimal(this.ubicacion.distanciaCon(ubicacion)*110.47).setScale(2,RoundingMode.HALF_UP);
    }
}



