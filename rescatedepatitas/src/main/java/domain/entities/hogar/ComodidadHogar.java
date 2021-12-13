package domain.entities.hogar;

import javax.persistence.*;

@Table
@Entity
public class ComodidadHogar {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "comodidad_id", referencedColumnName = "id")
    private Comodidad comodidad;


    public Long getId() {
        return id;
    }
}
