package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.AdicionarServicoOrdemDeServicoInput;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.exception.OrdemDeServicoNaoEncontradaException;
import br.com.techchallenge.mecanica.domain.exception.ServicoNaoEncontradoException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdicionarServicoNaOrdemDeServicoUseCase {

    private final OrdemDeServicoGateway ordemGateway;
    private final ServicoGateway servicoGateway;

    public OrdemDeServico executar(
            AdicionarServicoOrdemDeServicoInput input) {

        OrdemDeServico ordem = ordemGateway
                .buscarPorId(input.ordemDeServicoId())
                .orElseThrow(() -> new OrdemDeServicoNaoEncontradaException(
                        "Ordem de serviço não encontrada"));

        Servico servico = servicoGateway
                .buscarPorId(input.servicoId())
                .orElseThrow(() -> new ServicoNaoEncontradoException(
                        "Serviço não encontrado"));

        ordem.adicionarServico(
                new ServicoOrdemDeServico(
                        servico.getId(),
                        servico,
                        servico.getPreco()));

        return ordemGateway.salvar(ordem);
    }
}