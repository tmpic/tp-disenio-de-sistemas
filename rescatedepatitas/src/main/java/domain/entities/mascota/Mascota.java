package domain.entities.mascota;

import domain.entities.Organizacion.Organizacion;
import domain.entities.otros.Foto;
import domain.entities.personas.DatosDeContacto;
import domain.entities.personas.Duenio;
import domain.entities.personas.Persona;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Table
@Entity
public class Mascota {

    public Mascota() {

        fotos = new ArrayList<Foto>();
        caracteristicasMascotas = new ArrayList<CaracteristicasMascotas>();
    }

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String apodo;
    @Column
    private String nombre;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mascota_id", referencedColumnName = "id")
    private List<CaracteristicasMascotas> caracteristicasMascotas;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mascota_id", referencedColumnName = "id")
    private List<Foto> fotos;
    @Column
    private String descripcionFisica;
    @ManyToOne
    @JoinColumn(name = "duenio_id", referencedColumnName = "id")
    private Persona duenio;
    @Column
    private Integer edad;
    @ManyToOne
    @JoinColumn(name = "especie_id", referencedColumnName = "id")
    private Especie especie;
    @ManyToOne
    @JoinColumn(name = "raza_id", referencedColumnName = "id")
    private Raza raza;
    @Column
    private String sexo;
    @ManyToOne
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private Organizacion organizacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoMascota estado;


    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Persona getDuenio() {
        return duenio;
    }

    public void setDuenio(Persona duenio) {
        this.duenio = duenio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CaracteristicasMascotas> getCaracteristicasMascotas() {
        return caracteristicasMascotas;
    }

    public String getDescripcionFisica() {
        return descripcionFisica;
    }

    public Integer getEdad() {
        return edad;
    }

    public Especie getEspecie() {
        return especie;
    }

    public EstadoMascota getEstado() {
        return estado;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public void setCaracteristicasMascotas(List<CaracteristicasMascotas> caracteristicasMascotas) {
        this.caracteristicasMascotas = caracteristicasMascotas;
    }

    public void setDescripcionFisica(String descripcionFisica) {
        this.descripcionFisica = descripcionFisica;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setEstado(EstadoMascota estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public void agregarFotos(Foto... fotos) {
        Collections.addAll(this.fotos, fotos);
    }

    public void agregarCaracteristica(CaracteristicasMascotas... caracteristicasMascotas) {
        Collections.addAll(this.caracteristicasMascotas, caracteristicasMascotas);
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }
}
