/*package domain.entities.publicacion;

import domain.entities.personas.Persona;
import domain.entities.publicacion.Publicaciones;
import domain.entities.personas.DatosDeContacto;

public class GenerarPublicacionInteresadoAdopcion{


    public static void CrearPublicacion(PublicacionPedirAdopcion publicacion) {
        String linkBorrarPublicacion = "LINKBORRARPUBLICACION";
        PublicacionPedirAdopcion publicacionIntencionAdoptar = new PublicacionPedirAdopcion();

        publicacionIntencionAdoptar.autor = publicacion.autor;
        publicacionIntencionAdoptar.caracteristicas = publicacion.caracteristicas;
        publicacionIntencionAdoptar.ubicacion = publicacion.ubicacion;
        publicacionIntencionAdoptar.patio = publicacion.patio;
        publicacionIntencionAdoptar.tokenBorrarPublicacion = linkBorrarPublicacion;
        Publicaciones.publicacionesPedirAdopcion.add(publicacionIntencionAdoptar);


        publicacion.autor.getDatosDeContactos().forEach(contacto-> contacto.contactar(linkBorrarPublicacion));
    }

    public void EliminarPublicacion(String link,Persona autor, PublicacionPedirAdopcion publicacion){
        if(autor == publicacion.autor && link == publicacion.tokenBorrarPublicacion){
            Publicaciones.publicacionesPedirAdopcion.remove(publicacion);

        }
    };

}

 */