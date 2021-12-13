package validacionDeClave;

import ExcepcionesPersonalizadas.ExcepcionClaveSimple;
import LectorDeArchivos.LectorDeArchivos;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidadorCaracteres implements ValidadorDeClave{

	private static final String PASSWORD_PATTERN =
			"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	public void validarClave(String password) {
		Matcher matcher = pattern.matcher(password);
		if(!matcher.matches()){
			throw new ExcepcionClaveSimple();
			}
		// match

		}


	}


