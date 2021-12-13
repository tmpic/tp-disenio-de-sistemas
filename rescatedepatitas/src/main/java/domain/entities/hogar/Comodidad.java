package domain.entities.hogar;

import javax.persistence.*;

@Entity
@Table
public class Comodidad {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String nombre;


    public Long getId() {
        return id;
    }
}
