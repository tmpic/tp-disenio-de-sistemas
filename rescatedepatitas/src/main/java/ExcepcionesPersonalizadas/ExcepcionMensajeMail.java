package ExcepcionesPersonalizadas;

public class ExcepcionMensajeMail extends RuntimeException{
    public ExcepcionMensajeMail(){
        this("El mail");
    }

    public ExcepcionMensajeMail(String str){
        super(str + " no pudo ser enviado");
    }
}
