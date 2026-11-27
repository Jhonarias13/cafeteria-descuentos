package descuento;

public final class ReglasDescuento {

    public static final double MONTO_MINIMO = 10.0;
    public static final double PORCENTAJE = 0.15;

    public static final double MONTO_PEDIDO_GRANDE = 30.0;
    public static final double PORCENTAJE_PEDIDO_GRANDE = 0.20;

    // El límite es "mayor a $10": un pedido de $10 exactos no tiene descuento.
    public static final ReglaDescuento QUINCE_POR_CIENTO =
            total -> total > MONTO_MINIMO ? total * PORCENTAJE : 0.0;

    // Se agregó después sin tocar la calculadora, solo registrándola en App.
    public static final ReglaDescuento VEINTE_POR_CIENTO_PEDIDO_GRANDE =
            total -> total > MONTO_PEDIDO_GRANDE ? total * PORCENTAJE_PEDIDO_GRANDE : 0.0;

    private ReglasDescuento() {
    }
}
