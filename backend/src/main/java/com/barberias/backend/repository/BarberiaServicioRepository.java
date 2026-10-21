package com.barberias.backend.repository;

import com.barberias.backend.entity.BarberiaServicio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarberiaServicioRepository
        extends JpaRepository<BarberiaServicio, Long> {

    boolean existsByBarberia_IdBarberiaAndServicio_IdServicio(
            Long idBarberia,
            Long idServicio
    );

    List<BarberiaServicio>
    findByBarberia_IdBarberiaAndActivoTrue(
            Long idBarberia
    );

    List<BarberiaServicio>
    findByServicio_IdServicioAndActivoTrue(
            Long idServicio
    );

    List<BarberiaServicio>
    findByServicio_IdServicioAndBarberia_Zona_IdZonaAndActivoTrue(
            Long idServicio,
            Integer idZona
    );
}
