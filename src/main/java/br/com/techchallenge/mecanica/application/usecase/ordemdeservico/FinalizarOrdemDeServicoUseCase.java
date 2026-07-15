package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinalizarOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;
    private final NotificarAlteracaoStatusOrdemUseCase notificarAlteracaoStatusOrdemUseCase;

    public OrdemDeServico executar(UUID ordemId) {

        OrdemDeServico ordem = ordemGateway.buscarPorId(ordemId)
                .orElseThrow(() -> new OrdemDeServicoNaoEncontradaException(
                        "Ordem de serviço não encontrada"));

        ordem.finalizar(LocalDateTime.now());

        OrdemDeServico ordemAtualizada = ordemGateway.salvar(ordem);

        notificarAlteracaoStatusOrdemUseCase.executar(ordemAtualizada);

        return ordemAtualizada;
    }
}