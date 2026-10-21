package com.barberias.backend.dto;

import com.barberias.backend.entity.Rol;

public class LoginResponse {

    private Long idUsuario;
    private String nombres;
    private String correo;
    private Rol rol;

    private String token;
    private String tipoToken;

    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(
            Long idUsuario,
            String nombres,
            String correo,
            Rol rol,
            String token,
            String mensaje) {

        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.correo = correo;
        this.rol = rol;
        this.token = token;
        this.tipoToken = "Bearer";
        this.mensaje = mensaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public Rol getRol() {
        return rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getMensaje() {
        return mensaje;
    }
}
