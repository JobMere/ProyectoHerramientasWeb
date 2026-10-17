--
-- Creacion de la base de datos
--
CREATE DATABASE IF NOT EXISTS `SistemaBarberia` CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE `SistemaBarberia`;
--
--Creacion de las tablas
--
CREATE TABLE IF NOT EXISTS `Clientes` (
    `ID_Clientes` INT AUTO_INCREMENT PRIMARY KEY,
    `Nombres` VARCHAR(100) NOT NULL,
    `Apellidos` VARCHAR(100) NOT NULL,
    `Telefono` VARCHAR(15) NOT NULL,
    `CorreoElectronico` VARCHAR(100) NOT NULL UNIQUE,
    `Contraseña` VARCHAR(255) NOT NULL,
    `FotoPerfil` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `TipoCortes` (
    `ID_TipoCortes` INT AUTO_INCREMENT PRIMARY KEY,
    `Nombre` VARCHAR(100) NOT NULL,
    `FotoReferencia` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `ServiciosAdicionales` (
    `ID_ServiciosAdicionales` INT AUTO_INCREMENT PRIMARY KEY,
    `Nombre` VARCHAR(100) NOT NULL,
    `Descripcion` TEXT,
    `FotoReferencia` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `Zonas` (
    `ID_Zonas` INT AUTO_INCREMENT PRIMARY KEY,
    `Nombre` VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS `Barberias` (
    `ID_Barberias` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Zonas` INT NOT NULL,
    `Nombre` VARCHAR(100) NOT NULL,
    `Descripcion` TEXT,
    `Direccion` VARCHAR(255) NOT NULL,
    `FotoPerfil` VARCHAR(255),
    `Latitud` DECIMAL(10, 8) NOT NULL,
    `Longitud` DECIMAL(11, 8) NOT NULL,
    `Telefono` VARCHAR(15) NOT NULL,
    `CorreoElectronico` VARCHAR(100) NOT NULL UNIQUE,
    `Contraseña` VARCHAR(255) NOT NULL,
    `HorarioAtencion` VARCHAR(100) NOT NULL,
    `Calificacion` DECIMAL(3, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (`ID_Zonas`) REFERENCES `Zonas`(`ID_Zonas`)
);

CREATE TABLE IF NOT EXISTS `FotosBarberias` (
    `ID_Fotos` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Barberias` INT NOT NULL,
    `Foto` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`ID_Barberias`) REFERENCES `Barberias`(`ID_Barberias`)
);

CREATE TABLE IF NOT EXISTS `BarberiaTipoCorte` (
    `ID_BarberiaTipoCorte` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Barberias` INT NOT NULL,
    `ID_TipoCortes` INT NOT NULL,
    `Precio` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`ID_Barberias`) REFERENCES `Barberias`(`ID_Barberias`),
    FOREIGN KEY (`ID_TipoCortes`) REFERENCES `TipoCortes`(`ID_TipoCortes`),
    UNIQUE (`ID_Barberias`, `ID_TipoCortes`)
);

CREATE TABLE IF NOT EXISTS `BarberiaServicioAdicional` (
    `ID_BarberiaServicioAdicional` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Barberias` INT NOT NULL,
    `ID_ServiciosAdicionales` INT NOT NULL,
    `Precio` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`ID_Barberias`) REFERENCES `Barberias`(`ID_Barberias`),
    FOREIGN KEY (`ID_ServiciosAdicionales`) REFERENCES `ServiciosAdicionales`(`ID_ServiciosAdicionales`),
    UNIQUE (`ID_Barberias`, `ID_ServiciosAdicionales`)
);

CREATE TABLE IF NOT EXISTS `Citas` (
    `ID_Citas` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Clientes` INT NOT NULL,
    `ID_Barberias` INT NOT NULL,
    `FechaHora` DATETIME NOT NULL,
    `Estado` ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Completada') DEFAULT 'Pendiente',
    FOREIGN KEY (`ID_Clientes`) REFERENCES `Clientes`(`ID_Clientes`),
    FOREIGN KEY (`ID_Barberias`) REFERENCES `Barberias`(`ID_Barberias`)
);

CREATE TABLE IF NOT EXISTS `CitaTipoCorte` (
    `ID_CitaTipoCorte` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Citas` INT NOT NULL,
    `ID_TipoCortes` INT NOT NULL,
    `Precio` DECIMAL(10, 2) NOT NULL, -- copiado de Barberia_TipoCorte al momento de agendar
    FOREIGN KEY (`ID_Citas`) REFERENCES `Citas`(`ID_Citas`),
    FOREIGN KEY (`ID_TipoCortes`) REFERENCES `TipoCortes`(`ID_TipoCortes`),
    UNIQUE (`ID_Citas`, `ID_TipoCortes`)
);

CREATE TABLE IF NOT EXISTS `CitaServicioAdicional` (
    `ID_CitaServicioAdicional` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Citas` INT NOT NULL,
    `ID_ServiciosAdicionales` INT NOT NULL,
    `Precio` DECIMAL(10, 2) NOT NULL, -- copiado de Barberia_ServicioAdicional al momento de agendar
    FOREIGN KEY (`ID_Citas`) REFERENCES `Citas`(`ID_Citas`),
    FOREIGN KEY (`ID_ServiciosAdicionales`) REFERENCES `ServiciosAdicionales`(`ID_ServiciosAdicionales`),
    UNIQUE (`ID_Citas`, `ID_ServiciosAdicionales`)
);

CREATE TABLE IF NOT EXISTS `Pagos` (
    `ID_Pagos` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Citas` INT NOT NULL, -- el cliente se obtiene via Citas.ID_Clientes, ya no se repite aqui
    `Monto` DECIMAL(10, 2) NOT NULL,
    `FechaPago` DATETIME NOT NULL,
    `MetodoPago` ENUM('Efectivo', 'Tarjeta', 'Transferencia') NOT NULL,
    FOREIGN KEY (`ID_Citas`) REFERENCES `Citas`(`ID_Citas`)
);

CREATE TABLE IF NOT EXISTS `Resenas` (
    `ID_Resenas` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Clientes` INT NOT NULL,
    `ID_Barberias` INT NOT NULL,
    `ID_Citas` INT NOT NULL UNIQUE, -- una resena por cita, y solo si hubo cita real
    `Calificacion` DECIMAL(3, 2) NOT NULL,
    `Comentario` TEXT,
    `FechaResena` DATETIME NOT NULL,
    FOREIGN KEY (`ID_Clientes`) REFERENCES `Clientes`(`ID_Clientes`),
    FOREIGN KEY (`ID_Barberias`) REFERENCES `Barberias`(`ID_Barberias`),
    FOREIGN KEY (`ID_Citas`) REFERENCES `Citas`(`ID_Citas`)
);

CREATE TABLE IF NOT EXISTS `Comprobantes` (
    `ID_Comprobantes` INT AUTO_INCREMENT PRIMARY KEY,
    `ID_Pagos` INT NOT NULL UNIQUE,
    `TipoComprobante` ENUM('Boleta', 'Factura') NOT NULL,
    `Serie` VARCHAR(4) NOT NULL,
    `Numero` VARCHAR(8) NOT NULL,
    `DNI` VARCHAR(8), -- obligatorio si TipoComprobante = Boleta
    `RUC` VARCHAR(11), -- obligatorio si TipoComprobante = Factura
    `RazonSocial` VARCHAR(150), -- obligatorio si TipoComprobante = Factura
    `Subtotal` DECIMAL(10, 2) NOT NULL,
    `IGV` DECIMAL(10, 2) NOT NULL,
    `Total` DECIMAL(10, 2) NOT NULL,
    `FechaEmision` DATETIME NOT NULL,
    FOREIGN KEY (`ID_Pagos`) REFERENCES `Pagos`(`ID_Pagos`),
    UNIQUE (`Serie`, `Numero`),
    CHECK (
        (`TipoComprobante` = 'Boleta' AND `DNI` IS NOT NULL)
        OR
        (`TipoComprobante` = 'Factura' AND `RUC` IS NOT NULL AND `RazonSocial` IS NOT NULL)
    )
);