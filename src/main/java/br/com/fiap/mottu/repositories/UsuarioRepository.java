package br.com.fiap.mottu.repositories;

import br.com.fiap.mottu.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	// Busca por nome ou CPF (simples convenience method para a interface web)
	List<Usuario> findByNomeContainingIgnoreCaseOrCpfContaining(String nome, String cpf);

}
