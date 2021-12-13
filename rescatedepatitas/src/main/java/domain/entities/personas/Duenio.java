package domain.entities.personas;

import com.google.common.hash.Hashing;

import javax.persistence.*;
import java.nio.charset.Charset;

@Entity
@Table
public class Duenio extends Persona {

    @Column
    private String nombreDeUsuario;
    @Column
    protected String password;

    public Duenio() {
        super();
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
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
}
