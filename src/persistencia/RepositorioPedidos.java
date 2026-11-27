package persistencia;

import descuento.ResultadoDescuento;
import modelo.Producto;

import java.sql.SQLException;
import java.util.List;

// La vista depende de esta interfaz y no de MySQL; cambiar de motor no la afecta.
public interface RepositorioPedidos {

    List<Producto> listarProductos() throws ClassNotFoundException, SQLException;

    int guardar(ResultadoDescuento resultado) throws ClassNotFoundException, SQLException;
}
