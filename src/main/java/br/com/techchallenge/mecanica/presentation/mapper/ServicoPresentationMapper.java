package br.com.techchallenge.mecanica.presentation.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.application.dto.servico.*;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.presentation.dto.servico.*;

@Component
public class ServicoPresentationMapper {

    public CriarServicoInput toInput(CriarServicoRequest request) {
        return new CriarServicoInput(
                request.descricao(),
                request.preco()
        );
    }

    public AtualizarServicoInput toInput(AtualizarServicoRequest request) {
        return new AtualizarServicoInput(
                request.descricao(),
                request.preco()
        );
    }

    public ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.isAtivo()
        );
    }
}