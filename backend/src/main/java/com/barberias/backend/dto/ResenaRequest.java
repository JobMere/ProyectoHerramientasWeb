package com.barberias.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResenaRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(
        value = 1,
        message = "La calificación mínima es 1"
    )
    @Max(
        value = 5,
        message = "La calificación máxima es 5"
    )
    private Integer calificacion;

    @Size(
        max = 500,
        message = "El comentario no puede superar 500 caracteres"
    )
    private String comentario;

    public ResenaRequest() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
}