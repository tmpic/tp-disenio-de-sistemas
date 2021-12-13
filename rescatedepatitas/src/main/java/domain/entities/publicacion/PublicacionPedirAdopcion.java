package domain.entities.publicacion;

import java.util.List;

import domain.entities.hogar.ComodidadHogar;
import domain.entities.hogar.Ubicacion;
import domain.entities.mascota.*;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Persona;

import javax.persistence.*;

@Entity
@DiscriminatorValue("pedirAdopcion")
public class PublicacionPedirAdopcion extends Publicacion {

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="publicacionPedirAdopcion_id", referencedColumnName = "id")
	private List<ComodidadHogar> comodidades;

	@Column
	private String tokenBorrarPublicacion;

	@ManyToOne
	@JoinColumn(name = "publicacionPedirAdopcion_especie_id", referencedColumnName = "id")
	private Especie especie_buscada;
	@ManyToOne
	@JoinColumn(name = "publicacionPedirAdopcion_raza_id", referencedColumnName = "id")
	private Raza raza_buscada;
	@Column
	String sexo;
	@Column
	Boolean patio;

	public PublicacionPedirAdopcion(Persona interesado,
									List<CaracteristicasMascotas> caracteristicas,
									boolean patio, String tokenBorrarPublicacion,
									Especie especie_buscada,
									Raza raza_buscada,
									String sexo,
									Ubicacion ubicacion) {
		super(interesado, caracteristicas, "Publicacion de adopcion", ubicacion);
		this.patio = patio;
		this.especie_buscada = especie_buscada;
		this.raza_buscada = raza_buscada;
		this.sexo = sexo;
	}

	public PublicacionPedirAdopcion() {

	}

	public List<ComodidadHogar> getComodidades() {
		return comodidades;
	}

	public void setComodidades(List<ComodidadHogar> comodidades) {
		this.comodidades = comodidades;
	}

	public String getTokenBorrarPublicacion() {
		return tokenBorrarPublicacion;
	}

	public void setTokenBorrarPublicacion(String tokenBorrarPublicacion) {
		this.tokenBorrarPublicacion = tokenBorrarPublicacion;
	}
}

