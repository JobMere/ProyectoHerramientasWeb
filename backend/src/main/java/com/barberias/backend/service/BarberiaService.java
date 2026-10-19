package com.barberias.backend.service;

import com.barberias.backend.dto.BarberiaRequest;
import com.barberias.backend.dto.BarberiaResponse;
import com.barberias.backend.entity.Barberia;
import com.barberias.backend.entity.Rol;
import com.barberias.backend.entity.Usuario;
import com.barberias.backend.entity.Zona;
import com.barberias.backend.repository.BarberiaRepository;
import com.barberias.backend.repository.UsuarioRepository;
import com.barberias.backend.repository.ZonaRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberiaService {

    private final BarberiaRepository barberiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ZonaRepository zonaRepository;

    public BarberiaService(
            BarberiaRepository barberiaRepository,
            UsuarioRepository usuarioRepository,
            ZonaRepository zonaRepository) {

        this.barberiaRepository = barberiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.zonaRepository = zonaRepository;
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
                .existsByUsuario_IdUsuario(usuario.getIdUsuario())) {

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

        barberia.setNombre(request.getNombre().trim());
        barberia.setDescripcion(request.getDescripcion());
        barberia.setDireccion(request.getDireccion().trim());
        barberia.setTelefono(request.getTelefono());
        barberia.setHorarioAtencion(request.getHorarioAtencion());
        barberia.setFotoUrl(request.getFotoUrl());
        barberia.setUsuario(usuario);
        barberia.setZona(zona);
        barberia.setActivo(true);

        return convertir(
                barberiaRepository.save(barberia)
        );
    }

    public List<BarberiaResponse> listar(Integer zonaId) {

        List<Barberia> barberias;

        if (zonaId != null) {
            barberias =
                    barberiaRepository
                            .findByZona_IdZonaAndActivoTrue(zonaId);
        } else {
            barberias =
                    barberiaRepository.findByActivoTrue();
        }

        return barberias
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

    private BarberiaResponse convertir(Barberia barberia) {

        return new BarberiaResponse(
                barberia.getIdBarberia(),
                barberia.getNombre(),
                barberia.getDescripcion(),
                barberia.getDireccion(),
                barberia.getTelefono(),
                barberia.getHorarioAtencion(),
                barberia.getFotoUrl(),
                barberia.getActivo(),
                barberia.getUsuario().getIdUsuario(),
                barberia.getZona().getIdZona(),
                barberia.getZona().getNombre()
        );
    }
}
