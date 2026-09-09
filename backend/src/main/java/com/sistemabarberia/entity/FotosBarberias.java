package com.sistemabarberia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "FotosBarberias")
public class FotosBarberias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Fotos")
    private Integer idFotos;

    // Relacion muchos-a-uno: muchas fotos pertenecen a una sola barberia
    @ManyToOne
    @JoinColumn(name = "ID_Barberias", nullable = false)
    private Barberias barberia;

    @Column(name = "Foto", nullable = false, length = 255)
    private String foto;

    public FotosBarberias() {
    }

    public FotosBarberias(Barberias barberia, String foto) {
        this.barberia = barberia;
        this.foto = foto;
    }

    public Integer getIdFotos() {
        return idFotos;
    }

    public void setIdFotos(Integer idFotos) {
        this.idFotos = idFotos;
    }

    public Barberias getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberias barberia) {
        this.barberia = barberia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}