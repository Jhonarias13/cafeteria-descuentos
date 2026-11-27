package descuento;

@FunctionalInterface
public interface ReglaDescuento {

    // Devuelve cuánto se descuenta (en dinero), no el total a pagar.
    double calcular(double total);
}
