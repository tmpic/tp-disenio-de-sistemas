package domain.entities.formaDeNotificacion;

import domain.entities.personas.DatosDeContacto;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "tipo_formaDeNotificacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class FormaDeNotificacion {
    @GeneratedValue
    @Id
    private Long id;

    public abstract void contactarDuenio(DatosDeContacto datos, String mensaje);

    public Long getId() {
        return id;
    }
}
