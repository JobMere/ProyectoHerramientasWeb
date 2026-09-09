package com.sistemabarberia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "BarberiaServicioAdicional",
    uniqueConstraints = @UniqueConstraint(columnNames = {"ID_Barberias", "ID_ServiciosAdicionales"})
)
public class BarberiaServicioAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BarberiaServicioAdicional")
    private Integer idBarberiaServicioAdicional;

    @ManyToOne
    @JoinColumn(name = "ID_Barberias", nullable = false)
    private Barberias barberia;

    @ManyToOne
    @JoinColumn(name = "ID_ServiciosAdicionales", nullable = false)
    private ServiciosAdicionales servicioAdicional;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    public BarberiaServicioAdicional() {
    }

    public BarberiaServicioAdicional(Barberias barberia, ServiciosAdicionales servicioAdicional, BigDecimal precio) {
        this.barberia = barberia;
        this.servicioAdicional = servicioAdicional;
        this.precio = precio;
    }

    public Integer getIdBarberiaServicioAdicional() {
        return idBarberiaServicioAdicional;
    }

    public void setIdBarberiaServicioAdicional(Integer idBarberiaServicioAdicional) {
        this.idBarberiaServicioAdicional = idBarberiaServicioAdicional;
    }

    public Barberias getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberias barberia) {
        this.barberia = barberia;
    }

    public ServiciosAdicionales getServicioAdicional() {
        return servicioAdicional;
    }

    public void setServicioAdicional(ServiciosAdicionales servicioAdicional) {
        this.servicioAdicional = servicioAdicional;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}