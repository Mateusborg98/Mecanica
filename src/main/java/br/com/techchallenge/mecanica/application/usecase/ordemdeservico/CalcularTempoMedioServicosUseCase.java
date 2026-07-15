package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.TempoMedioServicoOutput;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalcularTempoMedioServicosUseCase {

    private final OrdemDeServicoGateway gateway;

    public List<TempoMedioServicoOutput> executar() {
        return gateway.calcularTempoMedioServicos();
    }
}
