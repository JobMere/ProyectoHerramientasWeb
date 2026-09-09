package com.sistemabarberia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TipoCortes")
public class TipoCortes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TipoCortes")
    private Integer idTipoCortes;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "FotoReferencia", length = 255)
    private String fotoReferencia;

    public TipoCortes() {
    }

    public TipoCortes(String nombre, String fotoReferencia) {
        this.nombre = nombre;
        this.fotoReferencia = fotoReferencia;
    }

    public Integer getIdTipoCortes() {
        return idTipoCortes;
    }

    public void setIdTipoCortes(Integer idTipoCortes) {
        this.idTipoCortes = idTipoCortes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoReferencia() {
        return fotoReferencia;
    }

    public void setFotoReferencia(String fotoReferencia) {
        this.fotoReferencia = fotoReferencia;
    }
}