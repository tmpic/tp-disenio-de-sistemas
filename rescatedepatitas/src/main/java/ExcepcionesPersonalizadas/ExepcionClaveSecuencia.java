package ExcepcionesPersonalizadas;

public class ExepcionClaveSecuencia extends RuntimeException{

    public ExepcionClaveSecuencia(){
        this("La password ingresada");
    }

    public ExepcionClaveSecuencia(String str){
        super(str + " posee secuencias de caracteres");
    }
}
