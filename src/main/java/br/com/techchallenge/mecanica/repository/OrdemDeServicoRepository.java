package br.com.techchallenge.mecanica.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.OrdemDeServico;

public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, UUID> {
    
}
