package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.CriarOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.EstoqueGateway;
import br.com.techchallenge.mecanica.application.gateway.OperadorGateway;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.EstoqueNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.OperadorNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.PecaNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.ServicoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.VeiculoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;
    private final ClienteGateway clienteGateway;
    private final VeiculoGateway veiculoGateway;
    private final OperadorGateway operadorGateway;
    private final ServicoGateway servicoGateway;
    private final PecaGateway pecaGateway;
    private final EstoqueGateway estoqueGateway;

    @Transactional
    public OrdemDeServico executar(CriarOrdemDeServicoInput input) {

        clienteGateway.buscarPorId(input.clienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException(
                        "Cliente não encontrado"));

        veiculoGateway.buscarPorId(input.veiculoId())
                .orElseThrow(() -> new VeiculoNaoEncontradoException(
                        "Veículo não encontrado"));

        operadorGateway.buscarPorId(input.operadorId())
                .orElseThrow(() -> new OperadorNaoEncontradoException(
                        "Operador não encontrado"));

        OrdemDeServico ordem = new OrdemDeServico(
                input.clienteId(),
                input.veiculoId(),
                input.operadorId());

        for (UUID servicoId : listaOuVazia(input.servicoIds())) {
            Servico servico = servicoGateway.buscarPorId(servicoId)
                    .orElseThrow(() -> new ServicoNaoEncontradoException(
                            "Serviço não encontrado"));

            ordem.adicionarServico(new ServicoOrdemDeServico(
                    null,
                    servico,
                    servico.getPreco()));
        }

        for (CriarOrdemDeServicoInput.PecaInput item : listaOuVazia(input.pecas())) {
            Peca peca = pecaGateway.buscarPorId(item.pecaId())
                    .orElseThrow(() -> new PecaNaoEncontradaException(
                            "Peça não encontrada"));

            Estoque estoque = estoqueGateway.buscarEstoquePorPecaId(item.pecaId())
                    .orElseThrow(() -> new EstoqueNaoEncontradoException(
                            "Estoque não encontrado"));

            estoque.registrarSaida(item.quantidade());
            estoqueGateway.salvar(estoque);

            ordem.adicionarPeca(new PecaOrdemDeServico(
                    null,
                    peca,
                    item.quantidade(),
                    peca.getPreco()));
        }

        return ordemGateway.salvar(ordem);
    }

    private <T> List<T> listaOuVazia(List<T> itens) {
        return itens == null ? List.of() : itens;
    }
}
