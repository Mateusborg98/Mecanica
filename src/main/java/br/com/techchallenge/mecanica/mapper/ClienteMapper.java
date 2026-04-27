package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResponseDto;
import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResumoDto;
import br.com.techchallenge.mecanica.dto.clienteDto.CreateClienteRequestDto;
import br.com.techchallenge.mecanica.dto.clienteDto.UpdateClienteRequestDto;
import br.com.techchallenge.mecanica.entity.Cliente;

@Component
public class ClienteMapper {

    public Cliente toEntity(CreateClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpfCnpj(dto.getCpfCnpj());
        cliente.setContato(dto.getContato());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }

    public ClienteResponseDto toResponse(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getContato(),
                cliente.getEmail());
    }

    public static ClienteResumoDto toResumo(Cliente cliente) {
        return new ClienteResumoDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj());
    }

    public void updateEntity(UpdateClienteRequestDto dto, Cliente entity) {
        entity.setNome(dto.getNome());
        entity.setContato(dto.getContato());
        entity.setEmail(dto.getEmail());
    }

}
