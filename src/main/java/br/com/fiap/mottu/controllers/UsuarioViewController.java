package br.com.fiap.mottu.controllers;

import br.com.fiap.mottu.dto.UsuarioForm;
import br.com.fiap.mottu.models.Endereco;
import br.com.fiap.mottu.models.Moto;
import br.com.fiap.mottu.models.Usuario;
import br.com.fiap.mottu.repositories.UsuarioRepository;
import br.com.fiap.mottu.service.UsuarioCachingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.util.Optional;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping("/usuarios/ui")
public class UsuarioViewController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired(required = false)
    private UsuarioCachingService cachingService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/list";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "q", required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            List<Usuario> results = usuarioRepository.findByNomeContainingIgnoreCaseOrCpfContaining(q, q);
            model.addAttribute("results", results);
        }
        model.addAttribute("q", q);
        return "usuarios/search";
    }

    @GetMapping("/exportar")
    public void exportarCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");
        var usuarios = usuarioRepository.findAll();
        try (var writer = response.getWriter()) {
            writer.println("cpf,nome,dataNascimento,cep,placa");
            var fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
            for (Usuario u : usuarios) {
                String data = u.getDataNascimento() != null ? fmt.format(u.getDataNascimento()) : "";
                String cep = (u.getEndereco() != null && u.getEndereco().getCep() != null) ? u.getEndereco().getCep().toString() : "";
                String placa = (u.getPlaca() != null) ? u.getPlaca().getPlaca() : "";
                // basic CSV escaping for quotes
                String nome = u.getNome() != null ? u.getNome().replace("\"", "'") : "";
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n", u.getCpf(), nome, data, cep, placa);
            }
        }
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        model.addAttribute("editing", false);
        model.addAttribute("formAction", "/usuarios/ui");
        return "usuarios/form";
    }

    @PostMapping
    public String criar(@ModelAttribute("usuarioForm") @Valid UsuarioForm form,
                        BindingResult binding,
                        RedirectAttributes ra,
                        Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("editing", false);
            model.addAttribute("formAction", "/usuarios/ui");
            return "usuarios/form";
        }

        Usuario usuario = new Usuario();
        usuario.setCpf(form.getCpf());
        usuario.setNome(form.getNome());

        usuario.setDataNascimento(java.sql.Date.valueOf(form.getDataNascimento()));

        Endereco end = new Endereco();
        end.setCep(form.getCep());
        end.setPais(form.getPais());
        end.setEstado(form.getEstado());
        end.setCidade(form.getCidade());
        end.setBairro(form.getBairro());
        end.setNumero(form.getNumero());
        end.setLogradouro(form.getLogradouro());
        end.setComplemento(form.getComplemento());
        usuario.setEndereco(end);

        Moto moto = new Moto();
        moto.setPlaca(form.getPlaca());
        moto.setCpf(form.getCpfMoto() == null || form.getCpfMoto().isBlank()
                ? form.getCpf()
                : form.getCpfMoto());
        moto.setNiv(form.getNiv());
        moto.setMotor(form.getMotor());
        moto.setRenavam(form.getRenavam());
        moto.setFipe(form.getFipe());
        usuario.setPlaca(moto);

        usuarioRepository.save(usuario);
        limparCacheUsuarios();

        ra.addFlashAttribute("msg", "Usuário cadastrado com sucesso!");
        return "redirect:/usuarios/ui";
    }

    @GetMapping("/{cpf}/editar")
    public String editar(@PathVariable String cpf,
                         Model model,
                         RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(cpf);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("msg", "Usuário não encontrado para edição.");
            return "redirect:/usuarios/ui";
        }

        Usuario usuario = opt.get();
        UsuarioForm form = new UsuarioForm();

        form.setCpf(usuario.getCpf());
        form.setNome(usuario.getNome());

        if (usuario.getDataNascimento() != null) {
            java.util.Date raw = usuario.getDataNascimento();
            java.time.LocalDate ld;
            if (raw instanceof java.sql.Date sqlDate) {
                ld = sqlDate.toLocalDate();
            } else {
                ld = raw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            form.setDataNascimento(ld);
        }

        if (usuario.getEndereco() != null) {
            var e = usuario.getEndereco();
            form.setCep(e.getCep());
            form.setPais(e.getPais());
            form.setEstado(e.getEstado());
            form.setCidade(e.getCidade());
            form.setBairro(e.getBairro());
            form.setNumero(e.getNumero());
            form.setLogradouro(e.getLogradouro());
            form.setComplemento(e.getComplemento());
        }

        if (usuario.getPlaca() != null) {
            var m = usuario.getPlaca();
            form.setPlaca(m.getPlaca());
            form.setCpfMoto(!m.getCpf().equals(usuario.getCpf()) ? m.getCpf() : null);
            form.setNiv(m.getNiv());
            form.setMotor(m.getMotor());
            form.setRenavam(m.getRenavam());
            form.setFipe(m.getFipe());
        }

        model.addAttribute("usuarioForm", form);
        model.addAttribute("editing", true);
        model.addAttribute("formAction", "/usuarios/ui/" + cpf + "/atualizar");
        return "usuarios/form";
    }

    @PostMapping("/{cpf}/atualizar")
    public String atualizar(@PathVariable String cpf,
                            @ModelAttribute("usuarioForm") @Valid UsuarioForm form,
                            BindingResult binding,
                            RedirectAttributes ra,
                            Model model) {
        Optional<Usuario> opt = usuarioRepository.findById(cpf);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("msg", "Usuário não encontrado para atualizar.");
            return "redirect:/usuarios/ui";
        }
        if (binding.hasErrors()) {
            model.addAttribute("editing", true);
            model.addAttribute("formAction", "/usuarios/ui/" + cpf + "/atualizar");
            return "usuarios/form";
        }

        Usuario usuario = opt.get();
        usuario.setNome(form.getNome());
        usuario.setDataNascimento(java.sql.Date.valueOf(form.getDataNascimento()));

        Endereco end = usuario.getEndereco();
        if (end == null) end = new Endereco();
        end.setCep(form.getCep());
        end.setPais(form.getPais());
        end.setEstado(form.getEstado());
        end.setCidade(form.getCidade());
        end.setBairro(form.getBairro());
        end.setNumero(form.getNumero());
        end.setLogradouro(form.getLogradouro());
        end.setComplemento(form.getComplemento());
        usuario.setEndereco(end);

        Moto moto = usuario.getPlaca();
        if (moto == null) moto = new Moto();
        moto.setPlaca(form.getPlaca());
        moto.setCpf(form.getCpfMoto() == null || form.getCpfMoto().isBlank()
                ? usuario.getCpf()
                : form.getCpfMoto());
        moto.setNiv(form.getNiv());
        moto.setMotor(form.getMotor());
        moto.setRenavam(form.getRenavam());
        moto.setFipe(form.getFipe());
        usuario.setPlaca(moto);

        usuarioRepository.save(usuario);
        limparCacheUsuarios();

        ra.addFlashAttribute("msg", "Usuário atualizado com sucesso!");
        return "redirect:/usuarios/ui";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{cpf}/deletar")
    public String deletar(@PathVariable String cpf, RedirectAttributes ra) {
        Optional<Usuario> opt = usuarioRepository.findById(cpf);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("msg", "Usuário não encontrado para deletar.");
            return "redirect:/usuarios/ui";
        }
        usuarioRepository.delete(opt.get());
        limparCacheUsuarios();
        ra.addFlashAttribute("msg", "Usuário deletado com sucesso!");
        return "redirect:/usuarios/ui";
    }

    private void limparCacheUsuarios() {
        if (cachingService != null) {
            try { cachingService.limparCache(); } catch (Exception ignored) {}
        }
    }
}