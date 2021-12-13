package domain.entities.publicacion;

import javax.persistence.*;

@Entity
@Table
public class RespuestaPosible {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String respuesta;

    public Long getId() {
        return id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
