package com.barberias.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "resenas",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"id_barberia", "id_usuario"}
        )
    }
)
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idResena;

    @ManyToOne
    @JoinColumn(
        name = "id_barberia",
        nullable = false
    )
    private Barberia barberia;

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(length = 500)
    private String comentario;

    @Column(
        name = "fecha_resena",
        nullable = false
    )
    private LocalDateTime fechaResena;

    @PrePersist
    public void prePersist() {

        if (fechaResena == null) {
            fechaResena = LocalDateTime.now();
        }
    }

    public Resena() {
    }

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public Barberia getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberia barberia) {
        this.barberia = barberia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaResena() {
        return fechaResena;
    }

    public void setFechaResena(
            LocalDateTime fechaResena) {

        this.fechaResena = fechaResena;
    }
}