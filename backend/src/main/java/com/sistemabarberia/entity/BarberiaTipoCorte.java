package com.sistemabarberia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "BarberiaTipoCorte",
    uniqueConstraints = @UniqueConstraint(columnNames = {"ID_Barberias", "ID_TipoCortes"})
)
public class BarberiaTipoCorte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BarberiaTipoCorte")
    private Integer idBarberiaTipoCorte;

    @ManyToOne
    @JoinColumn(name = "ID_Barberias", nullable = false)
    private Barberias barberia;

    @ManyToOne
    @JoinColumn(name = "ID_TipoCortes", nullable = false)
    private TipoCortes tipoCorte;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    public BarberiaTipoCorte() {
    }

    public BarberiaTipoCorte(Barberias barberia, TipoCortes tipoCorte, BigDecimal precio) {
        this.barberia = barberia;
        this.tipoCorte = tipoCorte;
        this.precio = precio;
    }

    public Integer getIdBarberiaTipoCorte() {
        return idBarberiaTipoCorte;
    }

    public void setIdBarberiaTipoCorte(Integer idBarberiaTipoCorte) {
        this.idBarberiaTipoCorte = idBarberiaTipoCorte;
    }

    public Barberias getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberias barberia) {
        this.barberia = barberia;
    }

    public TipoCortes getTipoCorte() {
        return tipoCorte;
    }

    public void setTipoCorte(TipoCortes tipoCorte) {
        this.tipoCorte = tipoCorte;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}