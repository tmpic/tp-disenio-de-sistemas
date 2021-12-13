package validacionDeClave;

import java.util.ArrayList;
import java.util.List;


public class ValidacionDeClave {

	public List<ValidadorDeClave> validadores = new ArrayList<ValidadorDeClave>();
	
	public ValidacionDeClave()
	{
		validadores.add(new ValidadorDeClaveLargo());
		validadores.add(new ValidadorDeClaveSecuencias());
		validadores.add(new ValidadorDeClaveTop10000Peores());
		validadores.add(new ValidadorCaracteres());
	}
	
	public void validarClave( String clave) {
		validadores.forEach(v -> v.validarClave(clave));
	}

}