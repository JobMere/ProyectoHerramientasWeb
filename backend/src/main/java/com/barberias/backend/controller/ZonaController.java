package com.barberias.backend.controller;

import com.barberias.backend.entity.Zona;
import com.barberias.backend.service.ZonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "*")
public class ZonaController {

    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    @GetMapping
    public List<Zona> listar() {
        return zonaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zona> buscarPorId(@PathVariable Integer id) {
        return zonaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Zona crear(@RequestBody Zona zona) {
        return zonaService.guardar(zona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zona> actualizar(
            @PathVariable Integer id,
            @RequestBody Zona zona) {

        return zonaService.buscarPorId(id)
                .map(zonaExistente -> {
                    zonaExistente.setNombre(zona.getNombre());
                    return ResponseEntity.ok(
                            zonaService.guardar(zonaExistente)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        if (zonaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        zonaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
