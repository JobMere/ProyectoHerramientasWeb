package com.barberias.backend.service;

import com.barberias.backend.entity.Zona;
import com.barberias.backend.repository.ZonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

    private final ZonaRepository zonaRepository;

    public ZonaService(ZonaRepository zonaRepository) {
        this.zonaRepository = zonaRepository;
    }

    public List<Zona> listar() {
        return zonaRepository.findAll();
    }

    public Optional<Zona> buscarPorId(Integer id) {
        return zonaRepository.findById(id);
    }

    public Zona guardar(Zona zona) {
        return zonaRepository.save(zona);
    }

    public void eliminar(Integer id) {
        zonaRepository.deleteById(id);
    }
}
