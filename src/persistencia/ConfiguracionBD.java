package persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record ConfiguracionBD(String url, String usuario, String clave) {

    public static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/cafeteria?connectTimeout=3000&serverTimezone=UTC";

    // Si el archivo no existe se usan los valores de una instalación local típica.
    public static ConfiguracionBD cargar(Path archivo) {
        Properties propiedades = new Properties();
        if (Files.exists(archivo)) {
            try (InputStream entrada = Files.newInputStream(archivo)) {
                propiedades.load(entrada);
            } catch (IOException e) {
                System.err.println("No se pudo leer " + archivo + ": " + e.getMessage());
            }
        }
        return new ConfiguracionBD(
                propiedades.getProperty("url", URL_POR_DEFECTO),
                propiedades.getProperty("usuario", "root"),
                propiedades.getProperty("clave", ""));
    }
}
