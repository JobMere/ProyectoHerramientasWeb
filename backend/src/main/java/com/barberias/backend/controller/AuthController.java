package com.barberias.backend.controller;

import com.barberias.backend.dto.AuthResponse;
import com.barberias.backend.dto.LoginRequest;
import com.barberias.backend.dto.LoginResponse;
import com.barberias.backend.dto.RegistroRequest;

import com.barberias.backend.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService =
                authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(
            @Valid
            @RequestBody
            RegistroRequest request) {

        try {

            AuthResponse response =
                    authService.registrar(
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

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid
            @RequestBody
            LoginRequest request) {

        try {

            LoginResponse response =
                    authService.login(
                            request
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            Map.of(
                                    "error",
                                    e.getMessage()
                            )
                    );
        }
    }
}