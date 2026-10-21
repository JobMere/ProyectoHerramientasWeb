package com.barberias.backend.controller;

import com.barberias.backend.entity.Servicio;
import com.barberias.backend.service.ServicioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "*")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public List<Servicio> listar() {
        return servicioService.listar();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Servicio servicio) {

        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(servicioService.crear(servicio));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
