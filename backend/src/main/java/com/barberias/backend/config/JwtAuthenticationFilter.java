package com.barberias.backend.config;

import com.barberias.backend.entity.Usuario;

import com.barberias.backend.repository.UsuarioRepository;

import com.barberias.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioRepository usuarioRepository) {

        this.jwtService =
                jwtService;

        this.usuarioRepository =
                usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)

            throws ServletException,
            IOException {

        String authorization =
                request.getHeader(
                        "Authorization"
                );

        if (authorization == null
                ||
                !authorization.startsWith(
                        "Bearer "
                )) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authorization.substring(7);

        try {

            String correo =
                    jwtService
                            .extraerCorreo(token);

            if (correo != null
                    &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                    == null) {

                Usuario usuario =
                        usuarioRepository
                                .findByCorreo(correo)
                                .orElse(null);

                if (usuario != null
                        &&
                        Boolean.TRUE.equals(
                                usuario.getActivo()
                        )
                        &&
                        jwtService.esValido(
                                token,
                                usuario
                        )) {

                    SimpleGrantedAuthority autoridad =
                            new SimpleGrantedAuthority(
                                    "ROLE_"
                                            +
                                            usuario
                                                    .getRol()
                                                    .name()
                            );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getCorreo(),
                                    null,
                                    List.of(
                                            autoridad
                                    )
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );
                }
            }

        } catch (Exception ignored) {

            // Si el JWT es inválido,
            // Spring Security rechazará
            // posteriormente la solicitud.
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}
