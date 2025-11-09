package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.services.MotoService;
import fiap.com.br.SentinelTrack.Application.services.PatioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/relatorios")
@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public class RelatorioController {

    private final MotoService motoService;
    private final PatioService patioService;

    public RelatorioController(MotoService motoService, PatioService patioService) {
        this.motoService = motoService;
        this.patioService = patioService;
    }

    @GetMapping
    public String index(Model model) {
        List<MotoDTO> motos = motoService.listarTodas();
        List<PatioDTO> patios = patioService.listar();
        
        // Estatísticas gerais
        long totalMotos = motos.size();
        long motosDisponiveis = motos.stream().filter(m -> "DISPONIVEL".equals(m.getStatus())).count();
        long motosEmUso = motos.stream().filter(m -> "EM_USO".equals(m.getStatus())).count();
        long motosManutencao = motos.stream().filter(m -> "MANUTENCAO".equals(m.getStatus())).count();
        
        // Taxas percentuais
        double taxaDisponiveis = totalMotos > 0 ? (motosDisponiveis * 100.0 / totalMotos) : 0;
        double taxaEmUso = totalMotos > 0 ? (motosEmUso * 100.0 / totalMotos) : 0;
        double taxaManutencao = totalMotos > 0 ? (motosManutencao * 100.0 / totalMotos) : 0;
        
        // Distribuição por pátio
        Map<Long, String> patioNomes = patios.stream()
            .collect(Collectors.toMap(PatioDTO::getId, PatioDTO::getNome));
        
        Map<Long, Long> distribuicao = motos.stream()
            .collect(Collectors.groupingBy(MotoDTO::getIdPatio, Collectors.counting()));
        
        List<Map<String, Object>> distribuicaoPatios = distribuicao.entrySet().stream()
            .map(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nome", patioNomes.getOrDefault(entry.getKey(), "Desconhecido"));
                item.put("quantidade", entry.getValue());
                item.put("percentual", totalMotos > 0 ? 
                    String.format("%.1f", (entry.getValue() * 100.0 / totalMotos)) : "0.0");
                return item;
            })
            .collect(Collectors.toList());
        
        model.addAttribute("totalMotos", totalMotos);
        model.addAttribute("motosDisponiveis", motosDisponiveis);
        model.addAttribute("motosEmUso", motosEmUso);
        model.addAttribute("motosManutencao", motosManutencao);
        model.addAttribute("taxaDisponiveis", String.format("%.1f", taxaDisponiveis));
        model.addAttribute("taxaEmUso", String.format("%.1f", taxaEmUso));
        model.addAttribute("taxaManutencao", String.format("%.1f", taxaManutencao));
        model.addAttribute("distribuicaoPatios", distribuicaoPatios);
        
        return "relatorios/index";
    }
}
