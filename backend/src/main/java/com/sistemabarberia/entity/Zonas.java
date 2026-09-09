package com.sistemabarberia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Zonas")
public class Zonas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Zonas")
    private Integer idZonas;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    public Zonas() {
    }

    public Zonas(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdZonas() {
        return idZonas;
    }

    public void setIdZonas(Integer idZonas) {
        this.idZonas = idZonas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}