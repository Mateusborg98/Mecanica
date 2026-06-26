package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregarOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;

    public OrdemDeServico executar(UUID ordemId) {

        OrdemDeServico ordem = ordemGateway.buscarPorId(ordemId)
                .orElseThrow(() -> new OrdemDeServicoNaoEncontradaException(
                        "Ordem de serviço não encontrada"));

        ordem.entregar();

        return ordemGateway.salvar(ordem);
    }
}