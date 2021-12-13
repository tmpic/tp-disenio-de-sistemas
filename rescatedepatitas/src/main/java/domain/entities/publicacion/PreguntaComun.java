package domain.entities.publicacion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("comun")
public class PreguntaComun extends Pregunta{

}
