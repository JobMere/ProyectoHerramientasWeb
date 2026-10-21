-- ============================================================
-- BARBERHUB - MODELO DE BASE DE DATOS V2
-- Proyecto: Sistema Web de Barberías
-- Motor: MySQL
-- Base de datos: barberias_db
--
-- Este modelo corresponde al backend desarrollado con:
-- Spring Boot + Spring Data JPA + Hibernate + MySQL
-- ============================================================


-- ============================================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ============================================================

CREATE DATABASE IF NOT EXISTS barberias_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE barberias_db;


-- ============================================================
-- 2. TABLA: usuarios
-- ============================================================
-- Almacena clientes, propietarios de barberías y administradores.
-- La contraseña se almacena cifrada mediante BCrypt.
-- ============================================================

CREATE TABLE IF NOT EXISTS usuarios (

    id_usuario BIGINT NOT NULL AUTO_INCREMENT,

    nombres VARCHAR(100) NOT NULL,

    apellidos VARCHAR(100) NOT NULL,

    correo VARCHAR(150) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    telefono VARCHAR(20),

    rol ENUM(
        'CLIENTE',
        'BARBERIA',
        'ADMIN'
    ) NOT NULL,

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_usuario),

    CONSTRAINT uk_usuarios_correo
        UNIQUE (correo)

) ENGINE = InnoDB;


-- ============================================================
-- 3. TABLA: Zonas
-- ============================================================
-- Contiene las zonas o distritos donde están ubicadas
-- las barberías.
-- ============================================================

CREATE TABLE IF NOT EXISTS Zonas (

    ID_Zonas INT NOT NULL AUTO_INCREMENT,

    Nombre VARCHAR(100) NOT NULL,

    PRIMARY KEY (ID_Zonas)

) ENGINE = InnoDB;


-- ============================================================
-- 4. TABLA: barberias
-- ============================================================
-- Cada barbería pertenece a un usuario con rol BARBERIA
-- y está asociada a una zona.
--
-- La restricción UNIQUE sobre id_usuario garantiza que
-- un usuario solamente pueda tener una barbería registrada.
-- ============================================================

CREATE TABLE IF NOT EXISTS barberias (

    id_barberia BIGINT NOT NULL AUTO_INCREMENT,

    nombre VARCHAR(150) NOT NULL,

    descripcion VARCHAR(500),

    direccion VARCHAR(200) NOT NULL,

    telefono VARCHAR(20),

    horario_atencion VARCHAR(150),

    foto_url VARCHAR(500),

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    id_usuario BIGINT NOT NULL,

    id_zona INT NOT NULL,

    PRIMARY KEY (id_barberia),

    CONSTRAINT uk_barberias_usuario
        UNIQUE (id_usuario),

    CONSTRAINT fk_barberias_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_barberias_zona
        FOREIGN KEY (id_zona)
        REFERENCES Zonas(ID_Zonas)
        ON UPDATE CASCADE
        ON DELETE RESTRICT

) ENGINE = InnoDB;


-- ============================================================
-- 5. TABLA: servicios
-- ============================================================
-- Catálogo general de servicios disponibles.
-- Ejemplos:
-- Corte clásico
-- Fade
-- Barba
-- Lavado capilar
-- ============================================================

CREATE TABLE IF NOT EXISTS servicios (

    id_servicio BIGINT NOT NULL AUTO_INCREMENT,

    nombre VARCHAR(100) NOT NULL,

    descripcion VARCHAR(300),

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_servicio),

    CONSTRAINT uk_servicios_nombre
        UNIQUE (nombre)

) ENGINE = InnoDB;


-- ============================================================
-- 6. TABLA: barberia_servicios
-- ============================================================
-- Resuelve la relación muchos a muchos entre barberías
-- y servicios.
--
-- También permite que cada barbería defina su propio precio.
-- ============================================================

CREATE TABLE IF NOT EXISTS barberia_servicios (

    id_barberia_servicio BIGINT NOT NULL AUTO_INCREMENT,

    id_barberia BIGINT NOT NULL,

    id_servicio BIGINT NOT NULL,

    precio DECIMAL(10,2) NOT NULL,

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_barberia_servicio),

    CONSTRAINT uk_barberia_servicio
        UNIQUE (
            id_barberia,
            id_servicio
        ),

    CONSTRAINT fk_barberia_servicios_barberia
        FOREIGN KEY (id_barberia)
        REFERENCES barberias(id_barberia)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_barberia_servicios_servicio
        FOREIGN KEY (id_servicio)
        REFERENCES servicios(id_servicio)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_barberia_servicios_precio
        CHECK (precio > 0)

) ENGINE = InnoDB;


-- ============================================================
-- 7. TABLA: resenas
-- ============================================================
-- Los usuarios con rol CLIENTE pueden calificar barberías.
--
-- La calificación debe estar entre 1 y 5.
--
-- Un cliente solamente puede registrar una reseña
-- por barbería.
-- ============================================================

CREATE TABLE IF NOT EXISTS resenas (

    id_resena BIGINT NOT NULL AUTO_INCREMENT,

    id_barberia BIGINT NOT NULL,

    id_usuario BIGINT NOT NULL,

    calificacion INT NOT NULL,

    comentario VARCHAR(500),

    fecha_resena DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_resena),

    CONSTRAINT uk_resena_usuario_barberia
        UNIQUE (
            id_barberia,
            id_usuario
        ),

    CONSTRAINT chk_resena_calificacion
        CHECK (
            calificacion >= 1
            AND calificacion <= 5
        ),

    CONSTRAINT fk_resenas_barberia
        FOREIGN KEY (id_barberia)
        REFERENCES barberias(id_barberia)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_resenas_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT

) ENGINE = InnoDB;


-- ============================================================
-- 8. ÍNDICES ADICIONALES
-- ============================================================
-- Mejoran las búsquedas realizadas frecuentemente por
-- el backend.
-- ============================================================

CREATE INDEX idx_barberias_zona
ON barberias(id_zona);

CREATE INDEX idx_barberias_activo
ON barberias(activo);

CREATE INDEX idx_servicios_activo
ON servicios(activo);

CREATE INDEX idx_barberia_servicios_barberia
ON barberia_servicios(id_barberia);

CREATE INDEX idx_barberia_servicios_servicio
ON barberia_servicios(id_servicio);

CREATE INDEX idx_resenas_barberia
ON resenas(id_barberia);

CREATE INDEX idx_resenas_usuario
ON resenas(id_usuario);


-- ============================================================
-- 9. MODELO RELACIONAL RESUMIDO
-- ============================================================
--
-- usuarios
--     |
--     | 1 : 1
--     |
-- barberias -------- N : 1 -------- Zonas
--     |
--     |
--     | N : N
--     |
-- servicios
--     ^
--     |
-- barberia_servicios
--
--
-- usuarios (CLIENTE)
--     |
--     | 1 : N
--     |
--   resenas
--     |
--     | N : 1
--     |
-- barberias
--
-- ============================================================


-- ============================================================
-- 10. CONSULTAS DE VERIFICACIÓN
-- ============================================================

SHOW TABLES;


-- Consultar usuarios
SELECT *
FROM usuarios;


-- Consultar zonas
SELECT *
FROM Zonas;


-- Consultar barberías
SELECT *
FROM barberias;


-- Consultar servicios
SELECT *
FROM servicios;


-- Consultar servicios y precios de cada barbería
SELECT
    b.nombre AS barberia,
    s.nombre AS servicio,
    bs.precio
FROM barberia_servicios bs
INNER JOIN barberias b
    ON bs.id_barberia = b.id_barberia
INNER JOIN servicios s
    ON bs.id_servicio = s.id_servicio
WHERE bs.activo = TRUE;


-- Consultar barberías con su zona
SELECT
    b.id_barberia,
    b.nombre AS barberia,
    b.direccion,
    z.Nombre AS zona
FROM barberias b
INNER JOIN Zonas z
    ON b.id_zona = z.ID_Zonas
WHERE b.activo = TRUE;


-- Consultar reseñas
SELECT
    r.id_resena,
    b.nombre AS barberia,
    CONCAT(
        u.nombres,
        ' ',
        u.apellidos
    ) AS cliente,
    r.calificacion,
    r.comentario,
    r.fecha_resena
FROM resenas r
INNER JOIN barberias b
    ON r.id_barberia = b.id_barberia
INNER JOIN usuarios u
    ON r.id_usuario = u.id_usuario
ORDER BY r.fecha_resena DESC;


-- Promedio de calificación por barbería
SELECT
    b.id_barberia,
    b.nombre,
    ROUND(
        AVG(r.calificacion),
        2
    ) AS promedio_calificacion,
    COUNT(r.id_resena) AS cantidad_resenas
FROM barberias b
LEFT JOIN resenas r
    ON b.id_barberia = r.id_barberia
GROUP BY
    b.id_barberia,
    b.nombre;