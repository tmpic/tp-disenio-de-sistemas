package validacionDeClave;

import ExcepcionesPersonalizadas.ExcepcionClaveCorta;

public class ValidadorDeClaveLargo implements ValidadorDeClave{

	private static final int LARGO_CLAVE_MINIMO = 8;
	
	public void validarClave(String clave)
	{
		if (clave.length() < LARGO_CLAVE_MINIMO)
		{
			throw new ExcepcionClaveCorta();
		}
	}
}
