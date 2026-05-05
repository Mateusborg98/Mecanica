package br.com.techchallenge.mecanica.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.entity.StatusServicoEnum;

public interface ServicoRepository extends JpaRepository<Servico, UUID> {

    List<Servico> findByDescricaoAndStatus(String descricao, StatusServicoEnum status);

}
