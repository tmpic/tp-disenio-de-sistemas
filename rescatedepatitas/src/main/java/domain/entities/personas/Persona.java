package domain.entities.personas;

import domain.entities.mascota.Mascota;
import domain.entities.otros.TipoDeDocumento;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Entity
@DiscriminatorColumn(name = "tipo_persona")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Persona {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="persona_id")
    protected List<DatosDeContacto> datosDeContactos;
    @Column
    private int numeroDocumento;
    @Enumerated(EnumType.STRING)
    private TipoDeDocumento tipoDocumento;
    @Column(columnDefinition = "DATE")
    protected Date fechaDeNacimiento;
    @Column
    protected String nombreYApellido;

    public Persona() {
        this.datosDeContactos = new ArrayList<>();
    }

    public void contactarMascotaEncontrada(Rescatista rescatista){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.mascotaEncontrada(rescatista));
    }

    public void contactarEsMiMascota(Duenio duenio, String token){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.esMiMascota(duenio, token));
    }

    public void contactarAlguienQuiereAdoptar(Duenio duenio, Mascota mascota, String token){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.contactarAlguienQuiereAdoptar(duenio, mascota,token));
    }

    public void contactarIntencionDeAdopcion(String token){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.intencionDeAdopcion(token));
    }

    public void contactar(String mensaje){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.contactar(mensaje));
    }

    public void enviarNotificacion(Rescatista rescatista){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.mascotaEncontrada(rescatista));
    }

    public void quiereAdoptar(List<DatosDeContacto> adoptante){
        this.getDatosDeContactos().forEach(datoDeContacto -> datoDeContacto.adoptarMascota(datoDeContacto));
    }
    public List<DatosDeContacto> getDatosDeContactos() {
        return datosDeContactos;
    }

    public void agregarDatosDeContacto(DatosDeContacto... datos) {

        Collections.addAll(this.datosDeContactos, datos);

    }

    public Long getId() {
        return id;
    }

    public void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }

    public void setDatosDeContactos(List<DatosDeContacto> datosDeContactos) {
        this.datosDeContactos = datosDeContactos;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public TipoDeDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public String getNombreYApellido() {
        return nombreYApellido;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public void setTipoDocumento(TipoDeDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}
