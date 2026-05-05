package br.com.techchallenge.mecanica.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;

public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, UUID> {

    List<OrdemDeServico> findByStatus(StatusOrdemDeServicoEnum status);
    
}
