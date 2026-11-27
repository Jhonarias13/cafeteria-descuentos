package descuento;

import java.util.Comparator;
import java.util.List;

public class CalculadoraDescuento {

    private final List<ReglaNombrada> reglas;

    public CalculadoraDescuento(List<ReglaNombrada> reglas) {
        this.reglas = List.copyOf(reglas);
    }

    public ResultadoDescuento calcular(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        // Los descuentos no se acumulan: gana la regla que más le ahorra al cliente.
        return reglas.stream()
                .map(r -> new ResultadoDescuento(total, redondear(r.regla().calcular(total)), r.nombre()))
                .filter(resultado -> resultado.descuento() > 0)
                .max(Comparator.comparingDouble(ResultadoDescuento::descuento))
                .orElse(new ResultadoDescuento(total, 0.0, "Sin descuento"));
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
