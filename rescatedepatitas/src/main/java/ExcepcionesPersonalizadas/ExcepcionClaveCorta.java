package ExcepcionesPersonalizadas;

public class ExcepcionClaveCorta extends RuntimeException{

    public ExcepcionClaveCorta(){
        this("La password ingresada");
    }

    public ExcepcionClaveCorta(String str){
        super(str + " Es demasiado corta");
    }
}



