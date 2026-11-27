package modelo;

public class Bebida extends Producto {

    public enum Tamano {
        PEQUENO(0.0),
        MEDIANO(0.50),
        GRANDE(1.00);

        private final double recargo;

        Tamano(double recargo) {
            this.recargo = recargo;
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
        return "Bebida " + tamano.name().toLowerCase();
    }
}
