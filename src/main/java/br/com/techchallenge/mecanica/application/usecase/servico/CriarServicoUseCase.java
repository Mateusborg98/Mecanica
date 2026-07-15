package br.com.techchallenge.mecanica.application.usecase.servico;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.servico.CriarServicoInput;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarServicoUseCase {

    private final ServicoGateway servicoGateway;

    public Servico executar(CriarServicoInput input) {

        Servico servico = new Servico(
                input.descricao(),
                input.preco());

        return servicoGateway.salvar(servico);
    }
}