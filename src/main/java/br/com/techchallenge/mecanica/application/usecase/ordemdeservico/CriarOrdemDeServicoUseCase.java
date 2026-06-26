package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.CriarOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.ClienteGateway;
import br.com.techchallenge.mecanica.application.gateway.OperadorGateway;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.VeiculoGateway;
import br.com.techchallenge.mecanica.domain.exception.ClienteNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.OperadorNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.exception.VeiculoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriarOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;
    private final ClienteGateway clienteGateway;
    private final VeiculoGateway veiculoGateway;
    private final OperadorGateway operadorGateway;

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

        return ordemGateway.salvar(ordem);
    }
}