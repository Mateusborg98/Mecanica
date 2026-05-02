package br.com.techchallenge.mecanica.service;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.dto.pecaDto.UpdatePecaRequestDto;

public interface PecaService {

    PecaResponseDto criar(CreatePecaRequestDto request);

    PecaResponseDto buscarPorId(UUID id);

    List<PecaResponseDto> listar();

    PecaResponseDto atualizar(UUID id, UpdatePecaRequestDto request);

    void deletar(UUID id);

    void registrarSaidaEstoque(UUID pecaId, int quantidade);

    void registrarEntradaEstoque(UUID pecaId, int quantidade);
}