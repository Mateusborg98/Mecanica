package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarPecaOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdicionarPecaNaOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;
    private final PecaGateway pecaGateway;
    private final EstoqueGateway estoqueGateway;

    @Transactional
    public OrdemDeServico executar(
            AdicionarPecaOrdemDeServicoInput input) {

        OrdemDeServico ordem = ordemGateway
                .buscarPorId(input.ordemDeServicoId())
                .orElseThrow(() -> new OrdemDeServicoNaoEncontradaException(
                        "Ordem de serviço não encontrada"));

        Peca peca = pecaGateway
                .buscarPorId(input.pecaId())
                .orElseThrow(() -> new PecaNaoEncontradaException(
                        "Peça não encontrada"));

        Estoque estoque = estoqueGateway
                .buscarEstoquePorPecaId(input.pecaId())
                .orElseThrow(() -> new EstoqueNaoEncontradoException(
                        "Estoque não encontrado"));

        estoque.registrarSaida(input.quantidade());

        estoqueGateway.salvar(estoque);

        ordem.adicionarPeca(
                new PecaOrdemDeServico(
                        input.pecaOrdemDeServicoId(),
                        peca,
                        input.quantidade(),
                        peca.getPreco()));

        return ordemGateway.salvar(ordem);
    }
}
