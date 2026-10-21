package com.barberias.backend.service;

import com.barberias.backend.dto.BarberiaRequest;
import com.barberias.backend.dto.BarberiaResponse;

import com.barberias.backend.entity.Barberia;
import com.barberias.backend.entity.Rol;
import com.barberias.backend.entity.Usuario;
import com.barberias.backend.entity.Zona;

import com.barberias.backend.repository.BarberiaRepository;
import com.barberias.backend.repository.BarberiaServicioRepository;
import com.barberias.backend.repository.UsuarioRepository;
import com.barberias.backend.repository.ZonaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberiaService {

    private final BarberiaRepository barberiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ZonaRepository zonaRepository;
    private final BarberiaServicioRepository barberiaServicioRepository;

    public BarberiaService(
            BarberiaRepository barberiaRepository,
            UsuarioRepository usuarioRepository,
            ZonaRepository zonaRepository,
            BarberiaServicioRepository barberiaServicioRepository) {

        this.barberiaRepository = barberiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.zonaRepository = zonaRepository;
        this.barberiaServicioRepository = barberiaServicioRepository;
    }

    public BarberiaResponse crear(BarberiaRequest request) {

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        if (usuario.getRol() != Rol.BARBERIA) {
            throw new IllegalArgumentException(
                    "El usuario debe tener rol BARBERIA"
            );
        }

        if (barberiaRepository
                .existsByUsuario_IdUsuario(
                        usuario.getIdUsuario()
                )) {

            throw new IllegalArgumentException(
                    "Este usuario ya tiene una barbería registrada"
            );
        }

        Zona zona = zonaRepository
                .findById(request.getZonaId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Zona no encontrada"
                        )
                );

        Barberia barberia = new Barberia();

        barberia.setNombre(
                request.getNombre().trim()
        );

        barberia.setDescripcion(
                request.getDescripcion()
        );

        barberia.setDireccion(
                request.getDireccion().trim()
        );

        barberia.setTelefono(
                request.getTelefono()
        );

        barberia.setHorarioAtencion(
                request.getHorarioAtencion()
        );

        barberia.setFotoUrl(
                request.getFotoUrl()
        );

        barberia.setUsuario(usuario);
        barberia.setZona(zona);
        barberia.setActivo(true);

        Barberia guardada =
                barberiaRepository.save(barberia);

        return convertir(guardada);
    }

    public List<BarberiaResponse> listar(
            Integer zonaId,
            Long servicioId) {

        /*
         * CASO 1:
         * Filtrar por zona + servicio
         *
         * Ejemplo:
         * /api/barberias?zonaId=1&servicioId=2
         */
        if (zonaId != null && servicioId != null) {

            return barberiaServicioRepository
                    .findByServicio_IdServicioAndBarberia_Zona_IdZonaAndActivoTrue(
                            servicioId,
                            zonaId
                    )
                    .stream()
                    .map(relacion ->
                            relacion.getBarberia()
                    )
                    .filter(barberia ->
                            Boolean.TRUE.equals(
                                    barberia.getActivo()
                            )
                    )
                    .map(this::convertir)
                    .toList();
        }

        /*
         * CASO 2:
         * Filtrar solamente por servicio
         *
         * Ejemplo:
         * /api/barberias?servicioId=2
         */
        if (servicioId != null) {

            return barberiaServicioRepository
                    .findByServicio_IdServicioAndActivoTrue(
                            servicioId
                    )
                    .stream()
                    .map(relacion ->
                            relacion.getBarberia()
                    )
                    .filter(barberia ->
                            Boolean.TRUE.equals(
                                    barberia.getActivo()
                            )
                    )
                    .map(this::convertir)
                    .toList();
        }

        /*
         * CASO 3:
         * Filtrar solamente por zona
         *
         * Ejemplo:
         * /api/barberias?zonaId=1
         */
        if (zonaId != null) {

            return barberiaRepository
                    .findByZona_IdZonaAndActivoTrue(
                            zonaId
                    )
                    .stream()
                    .map(this::convertir)
                    .toList();
        }

        /*
         * CASO 4:
         * Sin filtros.
         *
         * Ejemplo:
         * /api/barberias
         */
        return barberiaRepository
                .findByActivoTrue()
                .stream()
                .map(this::convertir)
                .toList();
    }

    public BarberiaResponse buscar(Long id) {

        Barberia barberia = barberiaRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Barbería no encontrada"
                        )
                );

        return convertir(barberia);
    }

    private BarberiaResponse convertir(
            Barberia barberia) {

        return new BarberiaResponse(
                barberia.getIdBarberia(),
                barberia.getNombre(),
                barberia.getDescripcion(),
                barberia.getDireccion(),
                barberia.getTelefono(),
                barberia.getHorarioAtencion(),
                barberia.getFotoUrl(),
                barberia.getActivo(),

                barberia.getUsuario()
                        .getIdUsuario(),

                barberia.getZona()
                        .getIdZona(),

                barberia.getZona()
                        .getNombre()
        );
    }
}
