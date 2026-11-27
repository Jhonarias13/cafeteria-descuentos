package vista;

import descuento.CalculadoraDescuento;
import descuento.ResultadoDescuento;
import modelo.CatalogoProductos;
import modelo.Producto;
import persistencia.ConexionBD;
import persistencia.RepositorioPedidos;
import persistencia.ResultadoConexion;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class VentanaCafeteria extends JFrame {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final CalculadoraDescuento calculadora;
    private final ConexionBD conexionBD;
    private final RepositorioPedidos repositorio;

    private final JComboBox<Producto> comboProductos = new JComboBox<>();
    private final JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
    private final JButton botonAgregar = new JButton("Agregar al pedido");
    private final JTextField campoMonto = new JTextField(12);
    private final JButton botonCalcular = new JButton("Calcular Descuento");
    private final JButton botonLimpiar = new JButton("Limpiar");
    private final JTextArea areaResultado = new JTextArea(14, 48);
    private final JLabel etiquetaTotal = new JLabel("Total a pagar: -");

    private boolean baseDisponible;

    public VentanaCafeteria(CalculadoraDescuento calculadora, ConexionBD conexionBD, RepositorioPedidos repositorio) {
        super("Cafetería - Cálculo de descuentos");
        this.calculadora = calculadora;
        this.conexionBD = conexionBD;
        this.repositorio = repositorio;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(construirPanel());
        registrarEventos();
        pack();
        setMinimumSize(new Dimension(720, 600));
        setLocationRelativeTo(null);
    }

    private JPanel construirPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel titulo = new JLabel("Descuentos de la cafetería");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        JLabel subtitulo = new JLabel("Compras mayores a $10 tienen 15 % de descuento.");
        subtitulo.setForeground(new Color(90, 90, 90));
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);

        // El formulario va arriba con su alto natural y el área de mensajes ocupa el espacio que sobra.
        JPanel parteSuperior = new JPanel(new BorderLayout(10, 10));
        parteSuperior.add(encabezado, BorderLayout.NORTH);
        parteSuperior.add(construirFormulario(), BorderLayout.CENTER);

        panel.add(parteSuperior, BorderLayout.NORTH);
        panel.add(construirAreaResultado(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Pedido"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        formulario.add(new JLabel("Producto:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        formulario.add(comboProductos, c);
        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        formulario.add(spinnerCantidad, c);
        c.gridx = 3;
        formulario.add(botonAgregar, c);

        c.gridx = 0;
        c.gridy = 1;
        formulario.add(new JLabel("Monto total ($):"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(campoMonto, c);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        botones.add(botonCalcular);
        botones.add(botonLimpiar);
        c.gridx = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        formulario.add(botones, c);

        etiquetaTotal.setFont(etiquetaTotal.getFont().deriveFont(Font.BOLD, 16f));
        etiquetaTotal.setForeground(new Color(0, 110, 60));
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(etiquetaTotal, c);

        comboProductos.setPrototypeDisplayValue(CatalogoProductos.porDefecto().get(4));
        campoMonto.setToolTipText("Escriba el total o agréguelo con los productos. Ej: 12.50");
        getRootPane().setDefaultButton(botonCalcular);
        return formulario;
    }

    private JScrollPane construirAreaResultado() {
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado y mensajes de conexión"));
        return scroll;
    }

    private void registrarEventos() {
        botonCalcular.addActionListener(evento -> calcularDescuento());
        botonAgregar.addActionListener(evento -> agregarProducto());
        botonLimpiar.addActionListener(evento -> {
            campoMonto.setText("");
            etiquetaTotal.setText("Total a pagar: -");
            campoMonto.requestFocusInWindow();
        });
    }

    // Se hace en segundo plano para que la ventana no se congele si MySQL tarda en responder.
    public void verificarConexion() {
        escribir("Intentando conectar con la base de datos...");
        new SwingWorker<ResultadoConexion, Void>() {
            @Override
            protected ResultadoConexion doInBackground() {
                return conexionBD.verificar();
            }

            @Override
            protected void done() {
                try {
                    ResultadoConexion resultado = get();
                    baseDisponible = resultado.exitosa();
                    escribir((resultado.exitosa() ? "[OK] " : "[ERROR] ") + resultado.mensaje());
                } catch (Exception e) {
                    escribir("[ERROR] Falló la verificación de la conexión: " + e.getMessage());
                }
                cargarProductos();
            }
        }.execute();
    }

    private void cargarProductos() {
        List<Producto> productos = CatalogoProductos.porDefecto();
        if (baseDisponible) {
            try {
                productos = repositorio.listarProductos();
                escribir("Se cargaron " + productos.size() + " productos desde MySQL.");
            } catch (ClassNotFoundException | SQLException e) {
                escribir("[ERROR] No se pudieron leer los productos: " + e.getMessage());
            }
        } else {
            escribir("Se usa el catálogo local; los pedidos no se guardarán.");
        }
        comboProductos.setModel(new DefaultComboBoxModel<>(productos.toArray(new Producto[0])));
    }

    private void agregarProducto() {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) {
            return;
        }
        int cantidad = (Integer) spinnerCantidad.getValue();
        double nuevoMonto = leerMontoOCero() + producto.calcularPrecio() * cantidad;
        campoMonto.setText(String.format(Locale.US, "%.2f", nuevoMonto));
        escribir(String.format("+ %d x %s", cantidad, producto));
    }

    private void calcularDescuento() {
        double monto;
        try {
            monto = leerMonto();
        } catch (NumberFormatException e) {
            escribir("[ERROR] Ingrese un monto válido, por ejemplo 12.50");
            campoMonto.requestFocusInWindow();
            return;
        }

        ResultadoDescuento resultado;
        try {
            resultado = calculadora.calcular(monto);
        } catch (IllegalArgumentException e) {
            escribir("[ERROR] " + e.getMessage());
            return;
        }

        escribir(String.format(Locale.US,
                "Subtotal $%.2f - descuento $%.2f (%s) = total $%.2f",
                resultado.subtotal(), resultado.descuento(), resultado.reglaAplicada(), resultado.totalFinal()));
        etiquetaTotal.setText(String.format(Locale.US, "Total a pagar: $%.2f  (ahorro $%.2f)",
                resultado.totalFinal(), resultado.descuento()));
        guardarPedido(resultado);
    }

    private void guardarPedido(ResultadoDescuento resultado) {
        if (!baseDisponible) {
            return;
        }
        try {
            int id = repositorio.guardar(resultado);
            escribir("Pedido #" + id + " guardado en la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            escribir("[ERROR] No se pudo guardar el pedido: " + e.getMessage());
        }
    }

    // Acepta coma o punto decimal porque en Colombia se escribe 12,50.
    private double leerMonto() {
        String texto = campoMonto.getText().trim().replace(',', '.');
        if (texto.isEmpty()) {
            throw new NumberFormatException("vacío");
        }
        return Double.parseDouble(texto);
    }

    private double leerMontoOCero() {
        try {
            return leerMonto();
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void escribir(String mensaje) {
        areaResultado.append("[" + LocalTime.now().format(HORA) + "] " + mensaje + "\n");
        areaResultado.setCaretPosition(areaResultado.getDocument().getLength());
    }
}
