package com.sistemabarberia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ServiciosAdicionales")
public class ServiciosAdicionales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ServiciosAdicionales")
    private Integer idServiciosAdicionales;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "FotoReferencia", length = 255)
    private String fotoReferencia;

    public ServiciosAdicionales() {
    }

    public ServiciosAdicionales(String nombre, String descripcion, String fotoReferencia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fotoReferencia = fotoReferencia;
    }

    public Integer getIdServiciosAdicionales() {
        return idServiciosAdicionales;
    }

    public void setIdServiciosAdicionales(Integer idServiciosAdicionales) {
        this.idServiciosAdicionales = idServiciosAdicionales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoReferencia() {
        return fotoReferencia;
    }

    public void setFotoReferencia(String fotoReferencia) {
        this.fotoReferencia = fotoReferencia;
    }
}