package ExcepcionesPersonalizadas;

import domain.entities.personas.Persona;

public class ExcepcionAdopcionNoAutorizada extends RuntimeException {

    public ExcepcionAdopcionNoAutorizada() {
        super("La publicacion No esta aprobada aún");
    }
    }