package modelo;

public class Bebida extends Producto {

    public enum Tamano {
        PEQUENO("pequeña", 0.0),
        MEDIANO("mediana", 0.50),
        GRANDE("grande", 1.00);

        private final String etiqueta;
        private final double recargo;

        Tamano(String etiqueta, double recargo) {
            this.etiqueta = etiqueta;
            this.recargo = recargo;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public double getRecargo() {
            return recargo;
        }
    }

    private final Tamano tamano;

    public Bebida(String nombre, double precioBase, Tamano tamano) {
        super(nombre, precioBase);
        this.tamano = tamano == null ? Tamano.MEDIANO : tamano;
    }

    public Tamano getTamano() {
        return tamano;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() + tamano.getRecargo();
    }

    @Override
    public String getCategoria() {
        return "Bebida " + tamano.getEtiqueta();
    }
}
