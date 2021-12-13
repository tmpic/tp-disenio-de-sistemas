package validacionDeClave;

import ExcepcionesPersonalizadas.ExcepcionClaveCaracteresRepetidos;
import ExcepcionesPersonalizadas.ExepcionClaveSecuencia;

public class ValidadorDeClaveSecuencias implements ValidadorDeClave {

	private static final String REGEX_CARACTERES_IGUALES = "^(.)\\1*$";
	private static final String SECUENCIAS = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzqwertyQWERTY";
	private static final int GRUPOS_SECUENCIAS = 3;

	public void validarClave(String clave) {

		validarCaracteresIguales(clave);
		validarCaracteresSecuenciales(clave);
	}

	private void validarCaracteresIguales(String clave) {
		if (clave.matches(REGEX_CARACTERES_IGUALES)) {
			throw new ExcepcionClaveCaracteresRepetidos();
		}
	}

	private void validarCaracteresSecuenciales(String clave) {

		// Tomo el primer caracter de la clave para acumular
		String subCadena = clave.substring(0, 1);

		// Tomo el primer caracter de la clave para comparar
		char c = clave.charAt(0);

		for (int i = 0; i < clave.length() - 1; i++) {

			// Valido si forma secuencia con el siguiente caracter
			if (c == clave.charAt(i + 1) - 1) {

				// Acumulo el caracter
				subCadena = subCadena + clave.charAt(i + 1);
			}

			else

			{
				// Se rompio la secuencia. Valido si lo acumulado es secuencia.

				if (subCadena.length() >= GRUPOS_SECUENCIAS && SECUENCIAS.contains(subCadena)) {
					throw new ExepcionClaveSecuencia();
				} else {

					// Limpio la cadena acumulada y grabdo pr�ximo caracter
					subCadena = Character.toString(clave.charAt(i + 1));
				}
			}

			// Validaci�n para �ltimo caracter
			if ((i == (clave.length() - 2)) && subCadena.length() >= GRUPOS_SECUENCIAS
					&& SECUENCIAS.contains(subCadena)) {
				throw new ExepcionClaveSecuencia();
			}

			// Guardo el pr�ximo caracter
			c = clave.charAt(i + 1);
		}
	}
}