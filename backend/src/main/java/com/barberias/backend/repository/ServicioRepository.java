package com.barberias.backend.repository;

import com.barberias.backend.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrue();

    boolean existsByNombreIgnoreCase(String nombre);
}