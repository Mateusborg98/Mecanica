package br.com.techchallenge.mecanica.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Peca;

public interface PecaRepository extends JpaRepository<Peca, UUID> {
    
}
