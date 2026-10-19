package com.barberias.backend.dto;

import java.time.LocalDateTime;

public class ResenaResponse {

    private Long idResena;

    private Long barberiaId;
    private String barberia;

    private Long usuarioId;
    private String usuario;

    private Integer calificacion;
    private String comentario;

    private LocalDateTime fechaResena;

    public ResenaResponse() {
    }

    public ResenaResponse(
            Long idResena,
            Long barberiaId,
            String barberia,
            Long usuarioId,
            String usuario,
            Integer calificacion,
            String comentario,
            LocalDateTime fechaResena) {

        this.idResena = idResena;
        this.barberiaId = barberiaId;
        this.barberia = barberia;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fechaResena = fechaResena;
    }

    public Long getIdResena() {
        return idResena;
    }

    public Long getBarberiaId() {
        return barberiaId;
    }

    public String getBarberia() {
        return barberia;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuario() {
        return usuario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getFechaResena() {
        return fechaResena;
    }
}
