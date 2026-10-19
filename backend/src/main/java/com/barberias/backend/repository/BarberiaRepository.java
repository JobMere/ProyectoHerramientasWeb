package com.barberias.backend.repository;

import com.barberias.backend.entity.Barberia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarberiaRepository
        extends JpaRepository<Barberia, Long> {

    boolean existsByUsuario_IdUsuario(Long idUsuario);

    List<Barberia> findByActivoTrue();

    List<Barberia> findByZona_IdZonaAndActivoTrue(Integer idZona);
}
