package creadorDeUsuario;

import domain.entities.personas.Voluntario;
import validacionDeClave.ValidadorDeClave;

public class CreadorDeUsuario {
    private ValidadorDeClave validadorDeClave;

    public Voluntario crearUsuarioVoluntario(String nombreDeUsuario, String contrasenia,String nombreYApellido){
        validadorDeClave.validarClave(contrasenia);

        Voluntario voluntario = new Voluntario();

        //voluntario.setNombreDeUsuario(nombreDeUsuario);
        //voluntario.setPassword(contrasenia);
        voluntario.setNombreYApellido(nombreYApellido);

        return voluntario;
    }
}
