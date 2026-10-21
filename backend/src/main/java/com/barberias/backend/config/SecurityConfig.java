package com.barberias.backend.config;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth -> auth

                    // Login y registro públicos
                    .requestMatchers(
                            "/api/auth/**"
                    )
                    .permitAll()

                    // Consultas públicas
                    .requestMatchers(
                            HttpMethod.GET,
                            "/api/**"
                    )
                    .permitAll()

                    // CORS
                    .requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .permitAll()

                    // Solo CLIENTE puede crear reseñas
                    .requestMatchers(
                            HttpMethod.POST,
                            "/api/barberias/*/resenas"
                    )
                    .hasRole("CLIENTE")

                    // BARBERIA o ADMIN puede asignar servicios
                    .requestMatchers(
                            HttpMethod.POST,
                            "/api/barberias/*/servicios"
                    )
                    .hasAnyRole(
                            "BARBERIA",
                            "ADMIN"
                    )

                    // BARBERIA o ADMIN puede registrar barberías
                    .requestMatchers(
                            HttpMethod.POST,
                            "/api/barberias"
                    )
                    .hasAnyRole(
                            "BARBERIA",
                            "ADMIN"
                    )

                    // BARBERIA o ADMIN puede crear servicios
                    .requestMatchers(
                            HttpMethod.POST,
                            "/api/servicios"
                    )
                    .hasAnyRole(
                            "BARBERIA",
                            "ADMIN"
                    )

                    // Lo demás requiere autenticación
                    .anyRequest()
                    .authenticated()
            )

            .exceptionHandling(exception ->
                    exception

                            // No inició sesión / JWT inválido
                            .authenticationEntryPoint(
                                    (request,
                                     response,
                                     authException) -> {

                                        response.setStatus(
                                                HttpServletResponse.SC_UNAUTHORIZED
                                        );

                                        response.setContentType(
                                                "application/json"
                                        );

                                        response.setCharacterEncoding(
                                                "UTF-8"
                                        );

                                        response.getWriter()
                                                .write(
                                                    "{\"error\":\"No autenticado o token inválido\"}"
                                                );
                                    }
                            )

                            // Está autenticado pero no tiene el rol
                            .accessDeniedHandler(
                                    (request,
                                     response,
                                     accessDeniedException) -> {

                                        response.setStatus(
                                                HttpServletResponse.SC_FORBIDDEN
                                        );

                                        response.setContentType(
                                                "application/json"
                                        );

                                        response.setCharacterEncoding(
                                                "UTF-8"
                                        );

                                        response.getWriter()
                                                .write(
                                                    "{\"error\":\"No tienes permisos para realizar esta operación\"}"
                                                );
                                    }
                            )
            )

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}