package br.com.techchallenge.mecanica.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;

public interface OrdemServicoService {

    OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request);

    OrdemDeServicoResponseDto buscarPorId(UUID id);

    List<OrdemDeServicoResponseDto> listar();

    OrdemDeServicoResponseDto iniciarDiagnostico(UUID id);

    OrdemDeServicoResponseDto enviarParaAprovacao(UUID id);

    OrdemDeServicoResponseDto aprovarOrcamento(UUID id);

    OrdemDeServicoResponseDto iniciarExecucao(UUID id);

    OrdemDeServicoResponseDto finalizar(UUID id);

    OrdemDeServicoResponseDto entregar(UUID id);

    Duration calcularTempoMedioExecucao();
}