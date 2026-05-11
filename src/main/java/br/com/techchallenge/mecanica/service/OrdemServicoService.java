package br.com.techchallenge.mecanica.service;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.AddServicoPecaOrdemDeServicoDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.TempoMedioServicoResponseDto;

public interface OrdemServicoService {

    OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request);

    OrdemDeServicoResponseDto buscarPorId(UUID id);

    List<OrdemDeServicoResponseDto> listar();

    OrdemDeServicoResponseDto iniciarDiagnostico(UUID id);

    OrdemDeServicoResponseDto aprovarOrcamento(UUID id);

    OrdemDeServicoResponseDto negarOrcamento(UUID id);

    OrdemDeServicoResponseDto finalizar(UUID id);

    OrdemDeServicoResponseDto entregar(UUID id);

    OrdemDeServicoResponseDto adicionarServicoPeca(UUID id, AddServicoPecaOrdemDeServicoDto dto);

    List<TempoMedioServicoResponseDto> calcularTempoMedioServicos();
}