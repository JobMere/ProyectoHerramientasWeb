package com.barberias.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Zonas")
    private Integer idZona;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    public Zona() {
    }

    public Zona(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
