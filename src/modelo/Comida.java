package modelo;

public class Comida extends Producto {

    public static final double COSTO_EMPAQUE = 0.30;

    private final boolean paraLlevar;

    public Comida(String nombre, double precioBase, boolean paraLlevar) {
        super(nombre, precioBase);
        this.paraLlevar = paraLlevar;
    }

    public boolean isParaLlevar() {
        return paraLlevar;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() + (paraLlevar ? COSTO_EMPAQUE : 0.0);
    }

    @Override
    public String getCategoria() {
        return paraLlevar ? "Comida para llevar" : "Comida";
    }
}
