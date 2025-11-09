package fiap.com.br.SentinelTrack.Api.controllers;

import fiap.com.br.SentinelTrack.Application.dto.CreateMotoDTO;
import fiap.com.br.SentinelTrack.Application.dto.MotoDTO;
import fiap.com.br.SentinelTrack.Application.services.MotoService;
import fiap.com.br.SentinelTrack.Application.services.PatioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/motos")
public class MotoWebController {

    private final MotoService motoService;
    private final PatioService patioService;

    public MotoWebController(MotoService motoService, PatioService patioService) {
        this.motoService = motoService;
        this.patioService = patioService;
    }

    @GetMapping
    public String listar(Model model, 
                        @RequestParam(required = false) String busca,
                        @RequestParam(required = false) String status,
                        @RequestParam(required = false) Long idPatio) {
        
        List<MotoDTO> motos;
        
        // Aplicar filtros
        if (status != null && !status.trim().isEmpty()) {
            motos = motoService.buscarPorStatus(status);
        } else if (idPatio != null) {
            motos = motoService.buscarPorPatio(idPatio);
        } else if (busca != null && !busca.trim().isEmpty()) {
            motos = motoService.buscarPorModelo(busca.trim());
            // Tentar buscar por placa também
            motoService.buscarPorPlaca(busca.trim()).ifPresent(motos::add);
        } else {
            motos = motoService.listarTodas();
        }
        
        // Estatísticas
        List<MotoDTO> todasMotos = motoService.listarTodas();
        long totalMotos = todasMotos.size();
        long motosDisponiveis = todasMotos.stream().filter(m -> "DISPONIVEL".equals(m.getStatus())).count();
        long motosEmUso = todasMotos.stream().filter(m -> "EM_USO".equals(m.getStatus())).count();
        long motosManutencao = todasMotos.stream().filter(m -> "MANUTENCAO".equals(m.getStatus())).count();
        
        model.addAttribute("motos", motos);
        model.addAttribute("patios", patioService.listar());
        model.addAttribute("busca", busca);
        model.addAttribute("status", status);
        model.addAttribute("idPatio", idPatio);
        model.addAttribute("totalMotos", totalMotos);
        model.addAttribute("motosDisponiveis", motosDisponiveis);
        model.addAttribute("motosEmUso", motosEmUso);
        model.addAttribute("motosManutencao", motosManutencao);
        
        return "motos/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERADOR')")
    public String novoForm(Model model) {
        model.addAttribute("moto", new CreateMotoDTO());
        model.addAttribute("patios", patioService.listar());
        model.addAttribute("isEdit", false);
        return "motos/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERADOR')")
    public String criar(@Valid @ModelAttribute("moto") CreateMotoDTO moto,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patios", patioService.listar());
            model.addAttribute("isEdit", false);
            return "motos/form";
        }

        try {
            motoService.criar(moto);
            redirectAttributes.addFlashAttribute("success", "Moto cadastrada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao cadastrar moto: " + e.getMessage());
            model.addAttribute("patios", patioService.listar());
            model.addAttribute("isEdit", false);
            return "motos/form";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERADOR')")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var motoOpt = motoService.buscarPorId(id);
        if (motoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Moto não encontrada!");
            return "redirect:/motos";
        }

        var moto = motoOpt.get();
        var createDTO = new CreateMotoDTO();
        createDTO.setModelo(moto.getModelo());
        createDTO.setPlaca(moto.getPlaca());
        createDTO.setStatus(moto.getStatus());
        createDTO.setDataEntrada(moto.getDataEntrada());
        createDTO.setIdPatio(moto.getIdPatio());

        model.addAttribute("moto", createDTO);
        model.addAttribute("motoId", id);
        model.addAttribute("patios", patioService.listar());
        model.addAttribute("isEdit", true);
        return "motos/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'OPERADOR')")
    public String atualizar(@PathVariable Long id,
                           @Valid @ModelAttribute("moto") CreateMotoDTO moto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.listar());
            model.addAttribute("isEdit", true);
            return "motos/form";
        }

        try {
            var updated = motoService.atualizar(id, moto);
            if (updated.isPresent()) {
                redirectAttributes.addFlashAttribute("success", "Moto atualizada com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Moto não encontrada!");
            }
            return "redirect:/motos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar moto: " + e.getMessage());
            model.addAttribute("motoId", id);
            model.addAttribute("patios", patioService.listar());
            model.addAttribute("isEdit", true);
            return "motos/form";
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (motoService.deletar(id)) {
                redirectAttributes.addFlashAttribute("success", "Moto removida com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Moto não encontrada!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao remover moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var motoOpt = motoService.buscarPorId(id);
        if (motoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Moto não encontrada!");
            return "redirect:/motos";
        }

        model.addAttribute("moto", motoOpt.get());
        return "motos/details";
    }
}
