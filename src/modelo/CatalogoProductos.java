package modelo;

import java.util.List;

public final class CatalogoProductos {

    private CatalogoProductos() {
    }

    // Se usa cuando la base de datos no responde, para que la caja pueda seguir vendiendo.
    public static List<Producto> porDefecto() {
        return List.of(
                new Bebida("Café americano", 2.50, Bebida.Tamano.MEDIANO),
                new Bebida("Capuchino", 3.20, Bebida.Tamano.GRANDE),
                new Bebida("Té chai", 2.80, Bebida.Tamano.PEQUENO),
                new Comida("Croissant", 2.20, false),
                new Comida("Sándwich de pollo", 5.50, true),
                new Comida("Brownie", 2.00, false));
    }
}
