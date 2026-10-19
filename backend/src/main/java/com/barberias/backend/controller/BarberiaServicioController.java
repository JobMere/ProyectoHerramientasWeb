package com.barberias.backend.controller;

import com.barberias.backend.dto.BarberiaServicioRequest;
import com.barberias.backend.dto.BarberiaServicioResponse;
import com.barberias.backend.service.BarberiaServicioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barberias/{barberiaId}/servicios")
@CrossOrigin(origins = "*")
public class BarberiaServicioController {

    private final BarberiaServicioService barberiaServicioService;

    public BarberiaServicioController(
            BarberiaServicioService barberiaServicioService) {

        this.barberiaServicioService = barberiaServicioService;
    }

    @PostMapping
    public ResponseEntity<?> asignar(
            @PathVariable Long barberiaId,
            @Valid @RequestBody BarberiaServicioRequest request) {

        try {

            BarberiaServicioResponse response =
                    barberiaServicioService.asignar(
                            barberiaId,
                            request
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

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
    public ResponseEntity<?> listar(
            @PathVariable Long barberiaId) {

        try {

            List<BarberiaServicioResponse> servicios =
                    barberiaServicioService.listar(
                            barberiaId
                    );

            return ResponseEntity.ok(servicios);

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
