package domain.entities.Organizacion;

import domain.entities.hogar.Ubicacion;
import domain.entities.personas.Voluntario;
import domain.entities.publicacion.Encuesta;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class Organizacion {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String nombreOrganizacion;
    @OneToOne()
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;
    @OneToMany
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private List<Encuesta> encuestas = new ArrayList<Encuesta>();
    @OneToOne
    @JoinColumn(name = "encuestaActiva_id", referencedColumnName = "id")
    private Encuesta encuestaActiva;


    public List<Encuesta> getEncuestas() {
        return encuestas;
    }

    public void agregarEncuesta(Encuesta encuestaAAgregar){
        encuestas.add(encuestaAAgregar);
    }

    public Encuesta getEncuestaActiva() {
        return encuestaActiva;
    }

    public void setEncuestaActiva(Encuesta encuestaActiva) {
        this.encuestaActiva = encuestaActiva;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Integer getId() {
        return id;
    }
}
