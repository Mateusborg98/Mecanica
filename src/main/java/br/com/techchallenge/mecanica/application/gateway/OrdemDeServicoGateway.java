package br.com.techchallenge.mecanica.application.gateway;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.TempoMedioServicoResponseDto;

public interface OrdemDeServicoGateway {

    OrdemDeServico salvar(OrdemDeServico ordemDeServico);

    OrdemDeServico buscarPorId(UUID id);

    List<OrdemDeServico> listar();

    OrdemDeServico iniciarDiagnostico(UUID id);

    OrdemDeServico aprovarOrcamento(UUID id);

    OrdemDeServico negarOrcamento(UUID id);

    OrdemDeServico finalizar(UUID id);

    OrdemDeServico entregar(UUID id);

    OrdemDeServico adicionarServicoPeca(UUID id, OrdemDeServico ordemDeServico);

    List<TempoMedioServicoResponseDto> calcularTempoMedioServicos();
}