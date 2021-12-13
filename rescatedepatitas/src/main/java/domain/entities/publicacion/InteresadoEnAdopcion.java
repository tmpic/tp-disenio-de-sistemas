package domain.entities.publicacion;
import ExcepcionesPersonalizadas.ExcepcionAdopcionNoAutorizada;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Persona;

public class InteresadoEnAdopcion {

    public static void QuiereAdoptar(Persona adoptante, PublicacionDarEnAdopcion publicacion){

        if(publicacion.estado == Estado.APROBADO) {
            publicacion.autor.quiereAdoptar(adoptante.getDatosDeContactos());
        }
        else {
            throw new ExcepcionAdopcionNoAutorizada();
        }
        }
    }


