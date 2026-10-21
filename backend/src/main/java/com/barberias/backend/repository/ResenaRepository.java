package com.barberias.backend.repository;

import com.barberias.backend.entity.Resena;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository
        extends JpaRepository<Resena, Long> {

    List<Resena>
    findByBarberia_IdBarberiaOrderByFechaResenaDesc(
            Long barberiaId
    );

    boolean existsByBarberia_IdBarberiaAndUsuario_IdUsuario(
            Long barberiaId,
            Long usuarioId
    );
}
