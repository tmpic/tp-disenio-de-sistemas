package domain.entities.publicacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import domain.entities.Organizacion.Organizacion;
import domain.entities.personas.Persona;
import domain.entities.mascota.CaracteristicasMascotas;
import domain.entities.hogar.Ubicacion;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "tipo_publicacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Publicacion {

	public Publicacion(Persona autor, List<CaracteristicasMascotas> caracteristicas, String descripcion, Ubicacion ubicacion) {
		this.autor = autor;
		this.caracteristicas = caracteristicas;
		this.descripcion = descripcion;
		this.estado = Estado.PENDIENTE;
		this.fecha = LocalDate.now();
		this.ubicacion = ubicacion;

	}

	public Publicacion(){
		this.caracteristicas = new ArrayList<CaracteristicasMascotas>();
	}

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "persona_id", referencedColumnName = "id")
	public Persona autor ;
	@ManyToOne
	@JoinColumn(name = "organizacion_id", referencedColumnName = "id")
	public Organizacion organizacion;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "publicacion_id", referencedColumnName = "id")
	public List<CaracteristicasMascotas> caracteristicas;
	@Column
	public String descripcion;
	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	public Estado estado;
	@Column(columnDefinition = "DATE")
	public LocalDate fecha;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
	public Ubicacion ubicacion;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "publicacion_id", referencedColumnName = "id")
	public List<PreguntaRespuesta> respuestas;

	public Persona getAutor() {
		return autor;
	}

	public void setId(Long id) {this.id = id; }

	public Long getId() {return id;}

	public List<CaracteristicasMascotas> getCaracteristicas() {
		return caracteristicas;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public void setAutor(Persona autor) {
		this.autor = autor;
	}

	public Organizacion getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	public void setCaracteristicas(List<CaracteristicasMascotas> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public void agregarCaracteristicas(List<CaracteristicasMascotas> caracteristicas){
		caracteristicas.stream().forEach(caracteristica -> this.caracteristicas.add(caracteristica));
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Estado getEstado() {
		return estado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public List<PreguntaRespuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<PreguntaRespuesta> respuestas) {
		this.respuestas = respuestas;
	}

}

	