package com.barberias.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BarberiaServicioRequest {

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(
        value = "0.01",
        message = "El precio debe ser mayor que cero"
    )
    private BigDecimal precio;

    public BarberiaServicioRequest() {
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
