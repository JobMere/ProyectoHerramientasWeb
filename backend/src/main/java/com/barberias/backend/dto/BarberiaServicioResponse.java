package com.barberias.backend.dto;

import java.math.BigDecimal;

public class BarberiaServicioResponse {

    private Long idBarberiaServicio;
    private Long barberiaId;
    private String barberia;
    private Long servicioId;
    private String servicio;
    private BigDecimal precio;
    private Boolean activo;

    public BarberiaServicioResponse() {
    }

    public BarberiaServicioResponse(
            Long idBarberiaServicio,
            Long barberiaId,
            String barberia,
            Long servicioId,
            String servicio,
            BigDecimal precio,
            Boolean activo) {

        this.idBarberiaServicio = idBarberiaServicio;
        this.barberiaId = barberiaId;
        this.barberia = barberia;
        this.servicioId = servicioId;
        this.servicio = servicio;
        this.precio = precio;
        this.activo = activo;
    }

    public Long getIdBarberiaServicio() {
        return idBarberiaServicio;
    }

    public Long getBarberiaId() {
        return barberiaId;
    }

    public String getBarberia() {
        return barberia;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public String getServicio() {
        return servicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Boolean getActivo() {
        return activo;
    }
}
