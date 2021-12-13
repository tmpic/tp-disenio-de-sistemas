package domain.entities.mascota;

import javax.persistence.*;

@Entity
@Table
public class Caracteristica {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String unaCaracteristica;


    public String getUnaCaracteristica() {
        return unaCaracteristica;
    }

    public void setUnaCaracteristica(String unaCaracteristica) {
        this.unaCaracteristica = unaCaracteristica;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
