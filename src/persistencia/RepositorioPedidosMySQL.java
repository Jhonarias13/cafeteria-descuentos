package persistencia;

import descuento.ResultadoDescuento;
import modelo.Bebida;
import modelo.Comida;
import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPedidosMySQL implements RepositorioPedidos {

    private static final String SQL_PRODUCTOS =
            "SELECT nombre, tipo, precio, tamano, para_llevar FROM productos ORDER BY tipo, nombre";

    private static final String SQL_GUARDAR =
            "INSERT INTO pedidos (subtotal, descuento, total, regla) VALUES (?, ?, ?, ?)";

    private final ConexionBD conexionBD;

    public RepositorioPedidosMySQL(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public List<Producto> listarProductos() throws ClassNotFoundException, SQLException {
        List<Producto> productos = new ArrayList<>();
        try (Connection conexion = conexionBD.abrir();
             Statement consulta = conexion.createStatement();
             ResultSet filas = consulta.executeQuery(SQL_PRODUCTOS)) {

            while (filas.next()) {
                productos.add(crearProducto(filas));
            }
        }
        return productos;
    }

    @Override
    public int guardar(ResultadoDescuento resultado) throws ClassNotFoundException, SQLException {
        try (Connection conexion = conexionBD.abrir();
             PreparedStatement insercion = conexion.prepareStatement(SQL_GUARDAR, Statement.RETURN_GENERATED_KEYS)) {

            insercion.setDouble(1, resultado.subtotal());
            insercion.setDouble(2, resultado.descuento());
            insercion.setDouble(3, resultado.totalFinal());
            insercion.setString(4, resultado.reglaAplicada());
            insercion.executeUpdate();

            try (ResultSet llaves = insercion.getGeneratedKeys()) {
                return llaves.next() ? llaves.getInt(1) : -1;
            }
        }
    }

    // La columna "tipo" decide qué subclase se crea; el resto del programa solo ve Producto.
    private Producto crearProducto(ResultSet fila) throws SQLException {
        String nombre = fila.getString("nombre");
        double precio = fila.getDouble("precio");

        if ("BEBIDA".equals(fila.getString("tipo"))) {
            String tamano = fila.getString("tamano");
            return new Bebida(nombre, precio, tamano == null ? null : Bebida.Tamano.valueOf(tamano));
        }
        return new Comida(nombre, precio, fila.getBoolean("para_llevar"));
    }
}
