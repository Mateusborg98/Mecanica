package br.com.techchallenge.mecanica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Estoque;
import br.com.techchallenge.mecanica.entity.Peca;

public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {

    Optional<Estoque> findByPeca(Peca peca);

    Object findByPecaId(UUID id);
    
}
