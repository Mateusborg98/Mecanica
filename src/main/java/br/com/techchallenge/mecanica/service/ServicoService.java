package br.com.techchallenge.mecanica.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.servicoDto.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.servicoDto.UpdateServicoRequestDTO;

public interface ServicoService {

    ServicoResponseDto criar(CreateServicoRequestDto request);

    ServicoResponseDto buscarPorId(UUID id);

    List<ServicoResponseDto> listar();

    ServicoResponseDto atualizar(UUID id, UpdateServicoRequestDTO request);

    void deletar(UUID id);

    Duration calcularTempoMedioPorServico(String descricao);
}