package modelo;

public abstract class Producto {

    private final String nombre;
    private final double precioBase;

    protected Producto(String nombre, double precioBase) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El producto necesita un nombre");
        }
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    // Cada subclase decide qué se suma al precio base (tamaño, empaque, etc.).
    public abstract double calcularPrecio();

    public abstract String getCategoria();

    @Override
    public String toString() {
        return String.format("%s (%s) - $%.2f", nombre, getCategoria(), calcularPrecio());
    }
}
