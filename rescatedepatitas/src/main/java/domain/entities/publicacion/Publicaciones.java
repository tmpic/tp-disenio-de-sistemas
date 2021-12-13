package domain.entities.publicacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import domain.entities.mascota.CaracteristicasMascotas;


public class Publicaciones {

	private List<PublicacionDarEnAdopcion> publicacionesDarEnAdopcion;
	private List<PublicacionDeMascotaEncontrada> publicacionesDeMascotaEncontrada;
	public static List<PublicacionPedirAdopcion> publicacionesPedirAdopcion;

	
	public void sugerirPublicaciones() {
		this.publicacionesPedirAdopcion
				.stream().forEach(p -> sugerir(p));
	}
	
	public void sugerir(Publicacion publicacion) {
		List<PublicacionDarEnAdopcion> recomendaciones = this.sugerirPublicacionesPara(publicacion.getCaracteristicas());
		String mensaje = "Hola, aca van los datos de las publicaciones recomendadas y el token para dar de baja la misma";
		if (recomendaciones != null) {
			publicacion.autor.contactar(mensaje);
			//Todo: darle formato al mail
		}
	}
	
	public List<PublicacionDarEnAdopcion> sugerirPublicacionesPara(List<CaracteristicasMascotas> caracteristicas){
		List<PublicacionDarEnAdopcion> recomendaciones = new ArrayList<PublicacionDarEnAdopcion>();
		recomendaciones = this.publicacionesDarEnAdopcion.
									stream().filter(p -> 
											p.getCaracteristicas().containsAll(caracteristicas))
									.collect(Collectors.toList());
		return recomendaciones;
	}
}
