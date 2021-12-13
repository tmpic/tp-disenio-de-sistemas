package domain.entities.mascota;

import javax.persistence.*;

@Entity
@Table
public class CaracteristicasMascotas {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "caracteristica_id", referencedColumnName = "id")
    private Caracteristica caracteristica;
    @Column
    private String descripcion;


    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }
}
