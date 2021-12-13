package validacionDeClave;

import java.io.IOException;

import ExcepcionesPersonalizadas.ExcepcionContraseniaDebil;
import LectorDeArchivos.LectorDeArchivos;

public class ValidadorDeClaveTop10000Peores implements ValidadorDeClave{
	private static final String LISTA_TOP10K_CLAVES = "target/classes/public/img/10K-Contrasena.txt";
	private LectorDeArchivos lector = new LectorDeArchivos(LISTA_TOP10K_CLAVES);

	public void validarClave(String password) {
		try {
			  if (lector.existeEnArchivo(password))
			  {
					throw new ExcepcionContraseniaDebil();
			  }
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
