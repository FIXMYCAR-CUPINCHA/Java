package br.com.fiap.mottu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.mottu.models.Patio;
import br.com.fiap.mottu.repositories.PatioRepository;

@Service
public class PatioService {

    private final PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }

    public List<Patio> listarTodos() {
        return patioRepository.findAll();
    }
}
