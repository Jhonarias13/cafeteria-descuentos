import descuento.CalculadoraDescuento;
import descuento.ReglaNombrada;
import descuento.ReglasDescuento;
import persistencia.ConexionBD;
import persistencia.ConfiguracionBD;
import persistencia.RepositorioPedidos;
import persistencia.RepositorioPedidosMySQL;
import vista.VentanaCafeteria;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Path;
import java.util.List;

public class App {

    public static void main(String[] args) {
        ConfiguracionBD configuracion = ConfiguracionBD.cargar(Path.of("db.properties"));
        ConexionBD conexion = new ConexionBD(configuracion);
        RepositorioPedidos repositorio = new RepositorioPedidosMySQL(conexion);

        // Para una regla nueva basta con sumarla a esta lista; la calculadora no se modifica.
        CalculadoraDescuento calculadora = new CalculadoraDescuento(List.of(
                new ReglaNombrada("15 % por compra mayor a $10", ReglasDescuento.QUINCE_POR_CIENTO),
                new ReglaNombrada("20 % por pedido mayor a $30", ReglasDescuento.VEINTE_POR_CIENTO_PEDIDO_GRANDE)));

        SwingUtilities.invokeLater(() -> {
            usarAparienciaDelSistema();
            VentanaCafeteria ventana = new VentanaCafeteria(calculadora, conexion, repositorio);
            ventana.setVisible(true);
            ventana.verificarConexion();
        });
    }

    private static void usarAparienciaDelSistema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Se usa la apariencia por defecto de Swing: " + e.getMessage());
        }
    }
}
