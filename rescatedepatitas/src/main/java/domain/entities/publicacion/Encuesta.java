package domain.entities.publicacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class Encuesta {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="encuesta_id")
    private List<Pregunta> preguntas = new ArrayList<Pregunta>();
    @Column
    private String descripcion;

    public void agregarPreguntas(List<Pregunta> preguntas) {
        preguntas.stream().forEach(pregunta -> this.agregarPregunta(pregunta));
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void agregarPregunta(Pregunta preguntaAAgregar){
        preguntas.add(preguntaAAgregar);
    }

    public void borrarPregunta(Pregunta preguntaABorrar) {preguntas.remove(preguntaABorrar);}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
