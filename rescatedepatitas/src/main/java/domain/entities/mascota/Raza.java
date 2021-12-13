package domain.entities.mascota;

import javax.persistence.*;

@Entity
@Table
public class Raza {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
