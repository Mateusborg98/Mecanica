package br.com.techchallenge.mecanica.application.usecase.servico;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListarServicosUseCase {

    private final ServicoGateway servicoGateway;

    public List<Servico> executar() {
        return servicoGateway.listar();
    }
}