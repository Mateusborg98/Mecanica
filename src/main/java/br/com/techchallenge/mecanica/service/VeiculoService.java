package br.com.techchallenge.mecanica.service;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;

public interface VeiculoService {

    VeiculoResponseDto criar(CreateVeiculoRequestDto request);

    VeiculoResponseDto buscarPorId(UUID id);

    VeiculoResponseDto buscarPorPlaca(String placa);

    List<VeiculoResponseDto> listar();

    VeiculoResponseDto atualizar(UUID id, UpdateVeiculoRequestDto request);

    void deletar(UUID id);
}