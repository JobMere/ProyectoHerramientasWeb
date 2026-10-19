package com.barberias.backend.service;

import com.barberias.backend.entity.Servicio;
import com.barberias.backend.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listar() {
        return servicioRepository.findByActivoTrue();
    }

    public Servicio crear(Servicio servicio) {

        String nombre = servicio.getNombre().trim();

        if (servicioRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe un servicio con ese nombre"
            );
        }

        servicio.setNombre(nombre);
        servicio.setActivo(true);

        return servicioRepository.save(servicio);
    }
}
