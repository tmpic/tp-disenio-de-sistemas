package domain.entities.publicacion;
import javax.persistence.*;

@Entity
@Table
public class PreguntaRespuesta {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "pregunta_id", referencedColumnName = "id")
    private Pregunta pregunta;
    @Column
    private String respuesta;

    public Pregunta getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Long getId() {
        return id;
    }
}
