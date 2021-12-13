package ExcepcionesPersonalizadas;

public class ExcepcionClaveSimple extends RuntimeException {


    public ExcepcionClaveSimple(){
        this("La password ingresada");
    }

    public ExcepcionClaveSimple(String str){
        super(str + "Debe contener al Menos una Mayuscula, una Minuscula y un Simbolo");
    }
}
