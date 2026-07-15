package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarItensOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarPecaOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarServicoOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdicionarItensNaOrdemDeServicoUseCase {
    private final OrdemDeServicoGateway ordemGateway;
    private final AdicionarServicoNaOrdemDeServicoUseCase adicionarServico;
    private final AdicionarPecaNaOrdemDeServicoUseCase adicionarPeca;

    @Transactional
    public OrdemDeServico executar(AdicionarItensOrdemDeServicoInput input) {
        OrdemDeServico ordem = ordemGateway.buscarPorId(input.ordemDeServicoId())
                .orElseThrow(() -> new OrdemDeServicoNaoEncontradaException(
                        "Ordem de serviço não encontrada"));
        for (UUID servicoId : listaOuVazia(input.servicoIds())) {
            ordem = adicionarServico.executar(new AdicionarServicoOrdemDeServicoInput(
                    input.ordemDeServicoId(), servicoId));
        }
        for (var peca : listaOuVazia(input.pecas())) {
            ordem = adicionarPeca.executar(new AdicionarPecaOrdemDeServicoInput(
                    null,
                    input.ordemDeServicoId(),
                    peca.pecaId(),
                    peca.quantidade()));
        }
        return ordem;
    }

    private <T> List<T> listaOuVazia(List<T> itens) {
        return itens == null ? List.of() : itens;
    }
}
