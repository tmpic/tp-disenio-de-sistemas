package domain.entities.publicacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.entities.hogar.Ubicacion;
import domain.entities.mascota.CaracteristicasMascotas;
import domain.entities.otros.Foto;
import domain.entities.personas.Persona;

import javax.persistence.*;

@Entity
@DiscriminatorValue("mascotaEncontrada")
public class PublicacionDeMascotaEncontrada extends Publicacion {
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "publicacion_id", referencedColumnName = "id")
	private List<Foto> fotos;
	@Column
	public String tokenBorrarPublicacion;

	public PublicacionDeMascotaEncontrada(Persona autor, List<CaracteristicasMascotas> caracteristicas, String descripcion, Ubicacion ubicacion) {
		super(autor, caracteristicas, descripcion, ubicacion);
		fotos = new ArrayList<Foto>();
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}

	public PublicacionDeMascotaEncontrada() {
		fotos = new ArrayList<Foto>();
	}

	public List<Foto> getFotos() {
		return fotos;
	}

	public void agregarFotos(Foto... fotos) {
		Collections.addAll(this.fotos, fotos);
	}

}
