package com.barberias.backend.service;

import com.barberias.backend.dto.ResenaRequest;
import com.barberias.backend.dto.ResenaResponse;

import com.barberias.backend.entity.Barberia;
import com.barberias.backend.entity.Resena;
import com.barberias.backend.entity.Rol;
import com.barberias.backend.entity.Usuario;

import com.barberias.backend.repository.BarberiaRepository;
import com.barberias.backend.repository.ResenaRepository;
import com.barberias.backend.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final BarberiaRepository barberiaRepository;
    private final UsuarioRepository usuarioRepository;

    public ResenaService(
            ResenaRepository resenaRepository,
            BarberiaRepository barberiaRepository,
            UsuarioRepository usuarioRepository) {

        this.resenaRepository = resenaRepository;
        this.barberiaRepository = barberiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ResenaResponse crear(
            Long barberiaId,
            ResenaRequest request) {

        Barberia barberia = barberiaRepository
                .findById(barberiaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Barbería no encontrada"
                        )
                );

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        if (usuario.getRol() != Rol.CLIENTE) {

            throw new IllegalArgumentException(
                    "Solo los clientes pueden registrar reseñas"
            );
        }

        boolean yaExiste =
                resenaRepository
                        .existsByBarberia_IdBarberiaAndUsuario_IdUsuario(
                                barberiaId,
                                usuario.getIdUsuario()
                        );

        if (yaExiste) {

            throw new IllegalArgumentException(
                    "El usuario ya registró una reseña para esta barbería"
            );
        }

        Resena resena = new Resena();

        resena.setBarberia(barberia);
        resena.setUsuario(usuario);
        resena.setCalificacion(
                request.getCalificacion()
        );
        resena.setComentario(
                request.getComentario()
        );

        return convertir(
                resenaRepository.save(resena)
        );
    }

    public List<ResenaResponse> listar(
            Long barberiaId) {

        if (!barberiaRepository.existsById(barberiaId)) {

            throw new IllegalArgumentException(
                    "Barbería no encontrada"
            );
        }

        return resenaRepository
                .findByBarberia_IdBarberiaOrderByFechaResenaDesc(
                        barberiaId
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    private ResenaResponse convertir(
            Resena resena) {

        String nombreUsuario =
                resena.getUsuario().getNombres()
                        + " "
                        + resena.getUsuario().getApellidos();

        return new ResenaResponse(
                resena.getIdResena(),

                resena.getBarberia()
                        .getIdBarberia(),

                resena.getBarberia()
                        .getNombre(),

                resena.getUsuario()
                        .getIdUsuario(),

                nombreUsuario,

                resena.getCalificacion(),
                resena.getComentario(),
                resena.getFechaResena()
        );
    }
}
