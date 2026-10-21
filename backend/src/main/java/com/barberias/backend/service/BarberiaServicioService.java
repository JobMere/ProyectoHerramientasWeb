package com.barberias.backend.service;

import com.barberias.backend.dto.BarberiaServicioRequest;
import com.barberias.backend.dto.BarberiaServicioResponse;
import com.barberias.backend.entity.Barberia;
import com.barberias.backend.entity.BarberiaServicio;
import com.barberias.backend.entity.Servicio;
import com.barberias.backend.repository.BarberiaRepository;
import com.barberias.backend.repository.BarberiaServicioRepository;
import com.barberias.backend.repository.ServicioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberiaServicioService {

    private final BarberiaServicioRepository barberiaServicioRepository;
    private final BarberiaRepository barberiaRepository;
    private final ServicioRepository servicioRepository;

    public BarberiaServicioService(
            BarberiaServicioRepository barberiaServicioRepository,
            BarberiaRepository barberiaRepository,
            ServicioRepository servicioRepository) {

        this.barberiaServicioRepository = barberiaServicioRepository;
        this.barberiaRepository = barberiaRepository;
        this.servicioRepository = servicioRepository;
    }

    public BarberiaServicioResponse asignar(
            Long barberiaId,
            BarberiaServicioRequest request) {

        Barberia barberia = barberiaRepository
                .findById(barberiaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Barbería no encontrada"
                        )
                );

        Servicio servicio = servicioRepository
                .findById(request.getServicioId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Servicio no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(servicio.getActivo())) {
            throw new IllegalArgumentException(
                    "El servicio no está activo"
            );
        }

        boolean existe =
                barberiaServicioRepository
                        .existsByBarberia_IdBarberiaAndServicio_IdServicio(
                                barberiaId,
                                request.getServicioId()
                        );

        if (existe) {
            throw new IllegalArgumentException(
                    "La barbería ya tiene asignado este servicio"
            );
        }

        BarberiaServicio relacion = new BarberiaServicio();

        relacion.setBarberia(barberia);
        relacion.setServicio(servicio);
        relacion.setPrecio(request.getPrecio());
        relacion.setActivo(true);

        BarberiaServicio guardado =
                barberiaServicioRepository.save(relacion);

        return convertir(guardado);
    }

    public List<BarberiaServicioResponse> listar(
            Long barberiaId) {

        if (!barberiaRepository.existsById(barberiaId)) {
            throw new IllegalArgumentException(
                    "Barbería no encontrada"
            );
        }

        return barberiaServicioRepository
                .findByBarberia_IdBarberiaAndActivoTrue(
                        barberiaId
                )
                .stream()
                .map(this::convertir)
                .toList();
    }

    private BarberiaServicioResponse convertir(
            BarberiaServicio relacion) {

        return new BarberiaServicioResponse(
                relacion.getIdBarberiaServicio(),
                relacion.getBarberia().getIdBarberia(),
                relacion.getBarberia().getNombre(),
                relacion.getServicio().getIdServicio(),
                relacion.getServicio().getNombre(),
                relacion.getPrecio(),
                relacion.getActivo()
        );
    }
}