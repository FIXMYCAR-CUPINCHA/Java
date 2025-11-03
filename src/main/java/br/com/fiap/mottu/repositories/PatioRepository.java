package br.com.fiap.mottu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.fiap.mottu.models.Patio;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Long> {

}
