package br.com.fiap.mottu.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.mottu.dto.PatioDTO;
import br.com.fiap.mottu.mappings.PatioMapper;
import br.com.fiap.mottu.service.PatioService;

@RestController
public class PatioController {

    private final PatioService patioService;
    private final PatioMapper patioMapper;

    public PatioController(PatioService patioService, PatioMapper patioMapper) {
        this.patioService = patioService;
        this.patioMapper = patioMapper;
    }

    @GetMapping("/patios")
    public List<PatioDTO> listarPatios() {
        return patioService.listarTodos()
                        .stream()
                        .map(patioMapper::toDTO)
                        .collect(Collectors.toList());
    }
}

