package com.barberias.backend.controller;

import com.barberias.backend.dto.BarberiaRequest;
import com.barberias.backend.dto.BarberiaResponse;

import com.barberias.backend.service.BarberiaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barberias")
@CrossOrigin(origins = "*")
public class BarberiaController {

    private final BarberiaService barberiaService;

    public BarberiaController(
            BarberiaService barberiaService) {

        this.barberiaService = barberiaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @Valid
            @RequestBody
            BarberiaRequest request) {

        try {

            BarberiaResponse barberia =
                    barberiaService.crear(
                            request
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(barberia);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping
    public List<BarberiaResponse> listar(
            @RequestParam(
                    required = false
            )
            Integer zonaId,

            @RequestParam(
                    required = false
            )
            Long servicioId) {

        return barberiaService.listar(
                zonaId,
                servicioId
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
            @PathVariable Long id) {

        try {

            BarberiaResponse barberia =
                    barberiaService.buscar(id);

            return ResponseEntity.ok(
                    barberia
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
}
