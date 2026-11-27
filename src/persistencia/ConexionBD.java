package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private final ConfiguracionBD configuracion;

    public ConexionBD(ConfiguracionBD configuracion) {
        this.configuracion = configuracion;
    }

    // Quien la pida debe cerrarla; por eso siempre se usa dentro de un try-with-resources.
    public Connection abrir() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(configuracion.url(), configuracion.usuario(), configuracion.clave());
    }

    public ResultadoConexion verificar() {
        try (Connection conexion = abrir();
             Statement consulta = conexion.createStatement();
             ResultSet resultado = consulta.executeQuery("SELECT VERSION(), DATABASE()")) {

            resultado.next();
            return new ResultadoConexion(true, String.format(
                    "Conexión exitosa a MySQL %s (base de datos: %s).",
                    resultado.getString(1), resultado.getString(2)));

        } catch (ClassNotFoundException e) {
            return new ResultadoConexion(false,
                    "No se encontró el driver de MySQL (" + e.getMessage() + ").\n"
                            + "Revise que lib/mysql-connector-j-9.1.0.jar esté en el classpath.");
        } catch (SQLException e) {
            return new ResultadoConexion(false,
                    "No se pudo conectar a la base de datos.\n"
                            + "Código " + e.getErrorCode() + " / estado SQL " + e.getSQLState() + ": " + e.getMessage() + "\n"
                            + "Revise que MySQL esté encendido y que exista la base 'cafeteria' (sql/cafeteria.sql).");
        }
    }
}
