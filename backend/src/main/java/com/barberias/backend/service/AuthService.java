package com.barberias.backend.service;

import com.barberias.backend.dto.AuthResponse;
import com.barberias.backend.dto.LoginRequest;
import com.barberias.backend.dto.RegistroRequest;
import com.barberias.backend.entity.Rol;
import com.barberias.backend.entity.Usuario;
import com.barberias.backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse registrar(RegistroRequest request) {

        String correo = request.getCorreo()
                .trim()
                .toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        // Por seguridad un usuario no puede registrarse
        // públicamente como ADMIN.
        if (request.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException(
                    "No está permitido registrarse como administrador"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidos(request.getApellidos().trim());
        usuario.setCorreo(correo);

        usuario.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        usuario.setTelefono(request.getTelefono());
        usuario.setRol(request.getRol());
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);

        return new AuthResponse(
                guardado.getIdUsuario(),
                guardado.getNombres(),
                guardado.getCorreo(),
                guardado.getRol(),
                "Usuario registrado correctamente"
        );
    }

    public AuthResponse login(LoginRequest request) {

        String correo = request.getCorreo()
                .trim()
                .toLowerCase();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Correo o contraseña incorrectos"
                        )
                );

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalArgumentException(
                    "La cuenta se encuentra deshabilitada"
            );
        }

        boolean passwordCorrecto = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPasswordHash()
        );

        if (!passwordCorrecto) {
            throw new IllegalArgumentException(
                    "Correo o contraseña incorrectos"
            );
        }

        return new AuthResponse(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol(),
                "Inicio de sesión correcto"
        );
    }
}
