package pruebas;

import descuento.CalculadoraDescuento;
import descuento.ReglaNombrada;
import descuento.ReglasDescuento;
import descuento.ResultadoDescuento;
import modelo.Bebida;
import modelo.Comida;
import modelo.Producto;

import java.util.List;
import java.util.function.BooleanSupplier;

public final class PruebasDescuento {

    private static int fallidas = 0;

    private PruebasDescuento() {
    }

    public static void main(String[] args) {
        CalculadoraDescuento soloQuince = new CalculadoraDescuento(List.of(
                new ReglaNombrada("15 %", ReglasDescuento.QUINCE_POR_CIENTO)));
        CalculadoraDescuento conPedidoGrande = new CalculadoraDescuento(List.of(
                new ReglaNombrada("15 %", ReglasDescuento.QUINCE_POR_CIENTO),
                new ReglaNombrada("20 %", ReglasDescuento.VEINTE_POR_CIENTO_PEDIDO_GRANDE)));

        verificar("$8 no tiene descuento", () -> soloQuince.calcular(8).descuento() == 0.0);

        // El límite es estricto: $10 exactos todavía no aplica.
        verificar("$10 exactos no tiene descuento", () -> soloQuince.calcular(10).descuento() == 0.0);
        verificar("$20 tiene $3 de descuento", () -> soloQuince.calcular(20).descuento() == 3.0);
        verificar("$20 paga $17", () -> soloQuince.calcular(20).totalFinal() == 17.0);
        verificar("$40 usa la regla de 20 % y no acumula", () -> {
            ResultadoDescuento r = conPedidoGrande.calcular(40);
            return r.descuento() == 8.0 && r.reglaAplicada().equals("20 %");
        });
        verificar("Monto negativo se rechaza", () -> {
            try {
                soloQuince.calcular(-1);
                return false;
            } catch (IllegalArgumentException esperado) {
                return true;
            }
        });

        Producto bebida = new Bebida("Café", 2.50, Bebida.Tamano.GRANDE);
        Producto comida = new Comida("Sándwich", 5.50, true);
        verificar("Bebida grande suma $1", () -> bebida.calcularPrecio() == 3.50);
        verificar("Comida para llevar suma empaque", () -> comida.calcularPrecio() == 5.80);

        System.out.println(fallidas == 0 ? "\nTodas las pruebas pasaron." : "\nPruebas fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static void verificar(String nombre, BooleanSupplier condicion) {
        boolean ok = condicion.getAsBoolean();
        if (!ok) {
            fallidas++;
        }
        System.out.println((ok ? " [OK]    " : " [FALLA] ") + nombre);
    }
}
