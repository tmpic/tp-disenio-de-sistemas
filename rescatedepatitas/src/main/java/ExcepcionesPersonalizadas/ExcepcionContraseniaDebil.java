package ExcepcionesPersonalizadas;

public class ExcepcionContraseniaDebil extends RuntimeException{

        public ExcepcionContraseniaDebil(){
            this("La password ingresada");
        }

        public ExcepcionContraseniaDebil(String str){
            super(str + " es debil");
        }
}
