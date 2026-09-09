package com.sistemabarberia.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Barberias")
public class Barberias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Barberias")
    private Integer idBarberias;

    // Relacion muchos-a-uno: muchas barberias pertenecen a una sola zona
    @ManyToOne
    @JoinColumn(name = "ID_Zonas", nullable = false)
    private Zonas zona;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "Direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "FotoPerfil", length = 255)
    private String fotoPerfil;

    @Column(name = "Latitud", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;

    @Column(name = "Longitud", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;

    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "Contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "HorarioAtencion", nullable = false, length = 100)
    private String horarioAtencion;

    @Column(name = "Calificacion", nullable = false, precision = 3, scale = 2)
    private BigDecimal calificacion = BigDecimal.ZERO;

    public Barberias() {
    }

    public Barberias(Zonas zona, String nombre, String descripcion, String direccion,
                      String fotoPerfil, BigDecimal latitud, BigDecimal longitud,
                      String telefono, String correoElectronico, String contrasena,
                      String horarioAtencion) {
        this.zona = zona;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.fotoPerfil = fotoPerfil;
        this.latitud = latitud;
        this.longitud = longitud;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.horarioAtencion = horarioAtencion;
    }

    public Integer getIdBarberias() {
        return idBarberias;
    }

    public void setIdBarberias(Integer idBarberias) {
        this.idBarberias = idBarberias;
    }

    public Zonas getZona() {
        return zona;
    }

    public void setZona(Zonas zona) {
        this.zona = zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigDecimal calificacion) {
        this.calificacion = calificacion;
    }
}