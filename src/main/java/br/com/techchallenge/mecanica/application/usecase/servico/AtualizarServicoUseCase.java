package br.com.techchallenge.mecanica.application.usecase.servico;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.servico.AtualizarServicoInput;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtualizarServicoUseCase {

    private final ServicoGateway servicoGateway;

    public Servico executar(UUID id, AtualizarServicoInput input) {

        Servico servico = servicoGateway.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servico.atualizarDados(
                input.descricao(),
                input.preco());

        return servicoGateway.salvar(servico);
    }
}