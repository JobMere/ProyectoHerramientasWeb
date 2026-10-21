package com.barberias.backend.controller;

import com.barberias.backend.dto.ResenaRequest;
import com.barberias.backend.dto.ResenaResponse;

import com.barberias.backend.service.ResenaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barberias/{barberiaId}/resenas")
@CrossOrigin(origins = "*")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(
            ResenaService resenaService) {

        this.resenaService = resenaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @PathVariable Long barberiaId,
            @Valid @RequestBody ResenaRequest request,
            Authentication authentication) {

        try {

            String correoUsuario =
                    authentication.getName();

            ResenaResponse response =
                    resenaService.crear(
                            barberiaId,
                            correoUsuario,
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

            List<ResenaResponse> resenas =
                    resenaService.listar(
                            barberiaId
                    );

            return ResponseEntity.ok(
                    resenas
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
