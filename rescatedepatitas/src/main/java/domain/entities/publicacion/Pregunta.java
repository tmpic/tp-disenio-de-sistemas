package domain.entities.publicacion;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "tipo_pregunta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Pregunta {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String unaPregunta;


    public String getUnaPregunta() {
        return unaPregunta;
    }

    public void setUnaPregunta(String unaPregunta) {
        this.unaPregunta = unaPregunta;
    }

    public Long getId() {
        return id;
    }
}