package com.barberias.backend.service;

import com.barberias.backend.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey obtenerClave() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generarToken(Usuario usuario) {

        Date ahora = new Date();

        Date expiracion =
                new Date(
                        ahora.getTime() + expirationMs
                );

        return Jwts.builder()
                .subject(usuario.getCorreo())

                .claim(
                        "idUsuario",
                        usuario.getIdUsuario()
                )

                .claim(
                        "rol",
                        usuario.getRol().name()
                )

                .issuedAt(ahora)
                .expiration(expiracion)

                .signWith(obtenerClave())

                .compact();
    }

    public String extraerCorreo(String token) {

        return extraerClaims(token)
                .getSubject();
    }

    public boolean esValido(
            String token,
            Usuario usuario) {

        String correo =
                extraerCorreo(token);

        Date expiracion =
                extraerClaims(token)
                        .getExpiration();

        return correo.equals(
                usuario.getCorreo()
        )
                &&
                expiracion.after(
                        new Date()
                );
    }

    private Claims extraerClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
