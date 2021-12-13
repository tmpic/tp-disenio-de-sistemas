package domain.entities.mascota;

import domain.entities.otros.Foto;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Especie {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String tipo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "especie_id", referencedColumnName = "id")
    private List<Raza> razas;

    public void setId(Long id) {
        this.id = id;
    }

    public List<Raza> getRazas() {
        return razas;
    }

    public void setRazas(List<Raza> razas) {
        this.razas = razas;
    }




    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }
}
