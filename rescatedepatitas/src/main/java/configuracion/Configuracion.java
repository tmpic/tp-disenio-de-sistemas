package configuracion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Configuracion {
    INSTANCE;
    private final Properties properties = new Properties();
    //"target/classes/public/config.properties"
    Configuracion() {
        try (InputStream input = new FileInputStream("target/classes/public/config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getPropiedad(String clave) {
        return properties.getProperty(clave);
    }
}
