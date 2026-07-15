package br.com.techchallenge.mecanica.application.usecase.servico;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InativarServicoUseCase {

    private final ServicoGateway servicoGateway;

    public void executar(UUID id) {

        Servico servico = servicoGateway.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        servico.inativar();

        servicoGateway.salvar(servico);
    }
}