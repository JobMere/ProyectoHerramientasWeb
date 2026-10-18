package com.barberias.backend.dto;

import com.barberias.backend.entity.Rol;

public class AuthResponse {

    private Long idUsuario;
    private String nombres;
    private String correo;
    private Rol rol;
    private String mensaje;

    public AuthResponse() {
    }

    public AuthResponse(
            Long idUsuario,
            String nombres,
            String correo,
            Rol rol,
            String mensaje) {

        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
