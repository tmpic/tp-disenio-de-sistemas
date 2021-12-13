package domain.entities.personas;

import configuracion.Configuracion;
import domain.entities.formaDeNotificacion.FormaDeNotificacion;
import domain.entities.mascota.Mascota;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class DatosDeContacto {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String email;
    @Column
    private String nombreYApellido;
    @Column
    private String telefono;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "datosDeContacto_id", referencedColumnName = "id")
    private List<FormaDeNotificacion> formasDeNotificacion;

    public DatosDeContacto(){
        formasDeNotificacion = new ArrayList<>();
    }

    public void contactar(String mensaje){
        formasDeNotificacion.forEach(formaDeNotificacion -> formaDeNotificacion.contactarDuenio(this, mensaje));
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreYApellido() {
        return nombreYApellido;
    }

    public void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<FormaDeNotificacion> getFormasDeNotificacion() {
        return formasDeNotificacion;
    }

    public void setFormasDeNotificacion(FormaDeNotificacion formasDeNotificacion) {
        this.formasDeNotificacion.add(formasDeNotificacion);
    }

    public void mascotaEncontrada(Rescatista rescatista){
        String mensaje = "hola " + this.getNombreYApellido() +", "+ rescatista.getNombreYApellido() + " encontro a tu mascota perdida. " +
                "Estos son sus datos de contacto:";
        for (DatosDeContacto datos: rescatista.datosDeContactos) {
            mensaje = mensaje + "\n\t" + "nombre: "  + datos.getNombreYApellido()  +" telefono: " + datos.getTelefono() + " Email: " + datos.getEmail();
        }

        this.contactar(mensaje);
    }

    public void esMiMascota(Duenio duenio, String token){
        String mensaje = "Hola " + this.getNombreYApellido() +", "+ duenio.getNombreYApellido() + " reclama que la mascota que encontraste" +
                " es suya y se quiere contactar con vos. " +
                "Estos son sus datos de contacto:";
        for (DatosDeContacto datos: duenio.datosDeContactos) {
            mensaje = mensaje + "\n\t" + "nombre: "  + datos.getNombreYApellido()  +" telefono: " + datos.getTelefono() + " Email: " + datos.getEmail();
        }

        String url = Configuracion.INSTANCE.getPropiedad("sistema.url") + "finalizarPublicacion/" + token;

        mensaje = String.format("%s\nPara dar por finalizada la publicación ingrese a este sitio: %s", mensaje, url);
        this.contactar(mensaje);
    }

    public void adoptarMascota(DatosDeContacto adoptante){
        this.contactar("Hola "+adoptante.getNombreYApellido() + "Esta interesado en adoptar tu mascota" + "Estos son sus datos de contacto"
        + adoptante.getTelefono() + "Email: " + getEmail() );
    }

    public void intencionDeAdopcion(String token){
        String mensaje = "Hola " + this.getNombreYApellido() + "su publicacion de intencion de adopcion fue generada correctamente";

        String url = Configuracion.INSTANCE.getPropiedad("sistema.url") + "finalizarPublicacion/" + token;

        mensaje = String.format("%s\nPara dar por finalizada la publicación ingrese a este sitio: %s", mensaje, url);
        this.contactar(mensaje);
    }



    public Long getId() {
        return id;
    }

    public void contactarAlguienQuiereAdoptar(Duenio duenio, Mascota mascota, String token) {
        String mensaje = "hola " + this.getNombreYApellido() +", "+ duenio.getNombreYApellido() + " esta interesado en adoptar a " + mascota.getNombre() +
                ".\n" +
                "Estos son sus datos de contacto:";
        for (DatosDeContacto datos: duenio.datosDeContactos) {
            mensaje = mensaje + "\n\t" + "nombre: "  + datos.getNombreYApellido()  +" telefono: " + datos.getTelefono() + " Email: " + datos.getEmail();
        }

        String url = Configuracion.INSTANCE.getPropiedad("sistema.url") + "finalizarPublicacion/" + token + "?dn=" + duenio.getId();

        mensaje = String.format("%s\nPara dar por finalizada la publicación y confirmar el cambio de dueño ingrese a este sitio: %s.\n Si solamente" +
                " quiere finalizar la publicación dirijase a misPublicaciones.", mensaje, url);
        this.contactar(mensaje);
    }
}
