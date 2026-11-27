package descuento;

public record ResultadoDescuento(double subtotal, double descuento, String reglaAplicada) {

    public double totalFinal() {
        return subtotal - descuento;
    }
}
