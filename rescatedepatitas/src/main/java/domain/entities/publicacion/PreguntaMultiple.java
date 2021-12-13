package domain.entities.publicacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@DiscriminatorValue("multiple")
public class PreguntaMultiple extends Pregunta{
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preguntaMultiple_id", referencedColumnName = "id")
    private List<RespuestaPosible> respuestasPosibles = new ArrayList<RespuestaPosible>();

    public List<RespuestaPosible> getRespuestasPosibles() {
        return respuestasPosibles;
    }

    public void agregarRespuesta(RespuestaPosible respuestaAAgregar){
        respuestasPosibles.add(respuestaAAgregar);
    }


}
