package br.com.techchallenge.mecanica.service;

import java.util.List;
import java.util.UUID;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResponseDto;
import br.com.techchallenge.mecanica.dto.clienteDto.CreateClienteRequestDto;
import br.com.techchallenge.mecanica.dto.clienteDto.UpdateClienteRequestDto;

public interface ClienteService {

    ClienteResponseDto criar(CreateClienteRequestDto request);

    ClienteResponseDto buscarPorId(UUID id);

    ClienteResponseDto buscarPorCpfCnpj(String cpfCnpj);

    List<ClienteResponseDto> listar();

    ClienteResponseDto atualizar(UUID id, UpdateClienteRequestDto request);

    void deletar(UUID id);
}