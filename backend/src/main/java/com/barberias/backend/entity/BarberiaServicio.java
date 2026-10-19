package com.barberias.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "barberia_servicios",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"id_barberia", "id_servicio"}
        )
    }
)
public class BarberiaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barberia_servicio")
    private Long idBarberiaServicio;

    @ManyToOne
    @JoinColumn(name = "id_barberia", nullable = false)
    private Barberia barberia;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean activo = true;

    public BarberiaServicio() {
    }

    public Long getIdBarberiaServicio() {
        return idBarberiaServicio;
    }

    public void setIdBarberiaServicio(Long idBarberiaServicio) {
        this.idBarberiaServicio = idBarberiaServicio;
    }

    public Barberia getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberia barberia) {
        this.barberia = barberia;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
