package domain.entities.publicacion;

import java.util.ArrayList;
import java.util.List;

import configuracion.Configuracion;
import domain.entities.hogar.Ubicacion;
import domain.entities.mascota.CaracteristicasMascotas;
import domain.entities.otros.Foto;
import domain.entities.personas.Persona;
import domain.entities.mascota.Mascota;

import javax.persistence.*;

@Entity
@DiscriminatorValue("darEnAdopcion")
public class PublicacionDarEnAdopcion extends Publicacion {
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="publicacion_id")
	private List<Foto> fotos;
	@OneToOne
	@JoinColumn(name = "mascota_id", referencedColumnName = "id")
	private Mascota mascota;

	public PublicacionDarEnAdopcion(Persona autor, List<CaracteristicasMascotas> caracteristicas, String descripcion, Ubicacion ubicacion) {
		super(autor, caracteristicas, descripcion, ubicacion);
	}
	public PublicacionDarEnAdopcion(){
		this.fotos = new ArrayList<Foto>();

	}

	public void agregarFotos(List<Foto> fotos){
		fotos.stream().forEach(foto -> {this.fotos.add(foto);});
	}

	public void setFotos(List<Foto> fotos){this.fotos = fotos;};

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setMascota(Mascota mascota) {this.mascota = mascota;};

	public Mascota getMascota() {
		return mascota;
	}

	public String Mensaje(){

	String ruta = Configuracion.INSTANCE.getPropiedad("sistema.url") + "detallePublicacionDarEnAdopcion/";
		return "el autor de esta publicacion es " + autor.getNombreYApellido() + " y la url es:  "+ ruta + getId().toString();
	}
}
