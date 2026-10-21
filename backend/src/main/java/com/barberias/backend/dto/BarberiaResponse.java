package com.barberias.backend.dto;

public class BarberiaResponse {

    private Long idBarberia;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String telefono;
    private String horarioAtencion;
    private String fotoUrl;
    private Boolean activo;

    private Long usuarioId;

    private Integer zonaId;
    private String zona;

    public BarberiaResponse() {
    }

    public BarberiaResponse(
            Long idBarberia,
            String nombre,
            String descripcion,
            String direccion,
            String telefono,
            String horarioAtencion,
            String fotoUrl,
            Boolean activo,
            Long usuarioId,
            Integer zonaId,
            String zona) {

        this.idBarberia = idBarberia;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horarioAtencion = horarioAtencion;
        this.fotoUrl = fotoUrl;
        this.activo = activo;
        this.usuarioId = usuarioId;
        this.zonaId = zonaId;
        this.zona = zona;
    }

    public Long getIdBarberia() {
        return idBarberia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public Boolean getActivo() {
        return activo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Integer getZonaId() {
        return zonaId;
    }

    public String getZona() {
        return zona;
    }
}
