package domain.entities.hogar;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table
public class Ubicacion {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    public String direccion;
    @Column
    public Double lat;
    @Column
    @SerializedName("long")
    public Double longitud;

    public Ubicacion(Double lat, Double longitud) {
        this.lat = lat;
        this.longitud = longitud;
    }
    public Ubicacion(){}

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @SerializedName("long")
    public double getLongitud() {
        return longitud;
    }

    @SerializedName("long")
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double distanciaCon(Ubicacion unaUbicacion){
        return Math.sqrt(Math.pow(unaUbicacion.lat - this.lat,2) + Math.pow(unaUbicacion.longitud - this.longitud,2));
    }

    public Long getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
