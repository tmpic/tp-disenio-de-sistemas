package ExcepcionesPersonalizadas;

public class ExcepcionClaveCaracteresRepetidos extends RuntimeException{
    public ExcepcionClaveCaracteresRepetidos(){
        this("La password ingresada");
    }

    public ExcepcionClaveCaracteresRepetidos(String str){
        super(str + " posee caracteres repetidos");
    }
}
