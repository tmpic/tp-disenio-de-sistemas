package domain.entities.personas;

import com.google.common.hash.Hashing;
import domain.entities.Organizacion.Organizacion;
import domain.entities.publicacion.Estado;
import domain.entities.publicacion.Publicacion;

import javax.persistence.*;
import java.nio.charset.Charset;

@Entity
@Table
public class Voluntario {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String nombreYApellido;
    @Column
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    public Organizacion organizacion;

    public Voluntario(){

    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Voluntario(String nombreYApellido, String password, Organizacion organizacion) {
        this.nombreYApellido = nombreYApellido;
        this.password = password;
        this.organizacion = organizacion;
    }

    public void aprobarPublicacion(Publicacion publicacion){
        publicacion.setEstado(Estado.APROBADO);
    }
    public String getNombreYApellido() {
        return nombreYApellido;
    }

    public void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String passHasheada = Hashing.md5().newHasher()
                .putString(password, Charset.defaultCharset())
                .hash()
                .toString();

        this.password = passHasheada;
    }

    public Long getId() {
        return id;
    }
}
