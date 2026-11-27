# Cafetería - Descuentos

Proyecto integrador (EA4) de Programación Orientada a Objetos II, IU Digital de Antioquia.

Aplicación de escritorio en Java (Swing) que calcula el descuento de los pedidos de una cafetería. Las compras mayores a $10 tienen 15 % de descuento. Al abrir, la aplicación intenta conectarse a una base de datos MySQL local, carga los productos desde ahí y guarda cada pedido calculado.

## Requisitos

- JDK 17 o superior.
- MySQL local encendido (probado con MySQL 9.7).
- El driver `lib/mysql-connector-j-9.1.0.jar` ya viene en el repositorio.

## Cómo ejecutarlo

1. Crear la base de datos y los productos de ejemplo:

```bash
mysql -u root -p < sql/cafeteria.sql
```

2. Si su usuario o clave de MySQL no son `root` sin clave, cambiarlos en `db.properties`.

3. Compilar, correr las pruebas y abrir la aplicación:

```bash
./ejecutar.sh      # macOS / Linux
ejecutar.bat       # Windows
```

Si MySQL está apagado o los datos de acceso son incorrectos, la aplicación abre igual, muestra el error en el área de mensajes y trabaja con un catálogo local sin guardar los pedidos.
