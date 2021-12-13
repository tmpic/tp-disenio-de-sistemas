package domain.entities.otros;

import javax.persistence.*;

@Entity
@Table
public class Foto {
    @Id
    @GeneratedValue
    private Long idFoto;
    @Column
    private String ruta;

    public Foto(){}

    public Foto(String ruta) {
        this.ruta = ruta;
    }


    public void setId(Long id) {
        this.idFoto = id;
    }

    public Long getId() {
        return idFoto;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
