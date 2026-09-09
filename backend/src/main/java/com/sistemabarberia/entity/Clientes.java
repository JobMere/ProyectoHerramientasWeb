package com.sistemabarberia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Clientes")
    private Integer idClientes;

    @Column(name = "Nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "Apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "Telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "CorreoElectronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "Contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "FotoPerfil", length = 255)
    private String fotoPerfil;

    // Constructor vacío, obligatorio para JPA
    public Clientes() {
    }

    // Constructor con todos los campos (util para crear objetos rapido en pruebas)
    public Clientes(String nombres, String apellidos, String telefono,
                     String correoElectronico, String contrasena, String fotoPerfil) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters y setters

    public Integer getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(Integer idClientes) {
        this.idClientes = idClientes;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}