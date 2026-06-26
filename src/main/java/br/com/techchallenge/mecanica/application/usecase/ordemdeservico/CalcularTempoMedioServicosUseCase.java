package br.com.techchallenge.mecanica.application.usecase.ordemdeservico;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.presentation.dto.ordemDeServico.TempoMedioServicoResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalcularTempoMedioServicosUseCase {

    private final OrdemDeServicoGateway gateway;

    public List<TempoMedioServicoResponseDto> executar() {
        return gateway.calcularTempoMedioServicos();
    }
}