-- Sin esta línea el cliente de MySQL puede guardar las tildes con otra codificación.
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS cafeteria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cafeteria;

CREATE TABLE IF NOT EXISTS productos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(60)              NOT NULL UNIQUE,
    tipo        ENUM('BEBIDA', 'COMIDA') NOT NULL,
    precio      DECIMAL(8, 2)            NOT NULL CHECK (precio >= 0),
    tamano      ENUM('PEQUENO', 'MEDIANO', 'GRANDE') NULL,
    para_llevar BOOLEAN                  NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS pedidos (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    subtotal  DECIMAL(10, 2) NOT NULL,
    descuento DECIMAL(10, 2) NOT NULL,
    total     DECIMAL(10, 2) NOT NULL,
    regla     VARCHAR(80)    NOT NULL,
    fecha     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- INSERT IGNORE permite volver a correr el script sin duplicar productos.
INSERT IGNORE INTO productos (nombre, tipo, precio, tamano, para_llevar) VALUES
    ('Café americano',     'BEBIDA', 2.50, 'MEDIANO', FALSE),
    ('Capuchino',          'BEBIDA', 3.20, 'GRANDE',  FALSE),
    ('Té chai',            'BEBIDA', 2.80, 'PEQUENO', FALSE),
    ('Jugo natural',       'BEBIDA', 3.00, 'MEDIANO', FALSE),
    ('Croissant',          'COMIDA', 2.20, NULL,      FALSE),
    ('Sándwich de pollo',  'COMIDA', 5.50, NULL,      TRUE),
    ('Brownie',            'COMIDA', 2.00, NULL,      FALSE),
    ('Ensalada César',     'COMIDA', 6.00, NULL,      TRUE);
