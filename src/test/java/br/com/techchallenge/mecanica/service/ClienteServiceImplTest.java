package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResponseDto;
import br.com.techchallenge.mecanica.dto.clienteDto.CreateClienteRequestDto;
import br.com.techchallenge.mecanica.dto.clienteDto.UpdateClienteRequestDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.service.implementation.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void deveCriarClienteComSucesso() {
        CreateClienteRequestDto request = new CreateClienteRequestDto("João da Silva", "12345678909", "11999999999",
                "João@gmail.com");

        when(clienteRepository.existsByCpfCnpj(request.getCpfCnpj()))
                .thenReturn(false);

        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ClienteResponseDto response = clienteService.criar(request);

        assertNotNull(response);
        assertEquals("João da Silva", response.getNome());
        assertEquals("12345678909", response.getCpfCnpj());

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void naoDeveCriarClienteComCpfCnpjDuplicado() {
        CreateClienteRequestDto request = new CreateClienteRequestDto("João da Silva", "12345678909", "11999999999",
                "João@gmail.com");

        when(clienteRepository.existsByCpfCnpj(request.getCpfCnpj()))
                .thenReturn(true);

        assertThrows(RegraNegocioException.class,
                () -> clienteService.criar(request));

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveBuscarClientePorCpfCnpj() {
        Cliente cliente = new Cliente();
        cliente.setCpfCnpj("12345678909");
        cliente.setNome("Maria");

        when(clienteRepository.findByCpfCnpj("12345678909"))
                .thenReturn(Optional.of(cliente));

        ClienteResponseDto response = clienteService.buscarPorCpfCnpj("12345678909");

        assertEquals("Maria", response.getNome());
    }

    @Test
    void deveFalharAoBuscarClienteInexistentePorCpfCnpj() {
        when(clienteRepository.findByCpfCnpj(any()))
                .thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> clienteService.buscarPorCpfCnpj("00000000000"));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setCpfCnpj("12345678909");

        UpdateClienteRequestDto request = new UpdateClienteRequestDto();
        request.setNome("Novo Nome");

        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(cliente));

        ClienteResponseDto response = clienteService.atualizar(id, request);

        assertEquals("Novo Nome", response.getNome());
    }

    @Test
    void deveFalharAoDeletarClienteInexistente() {
        UUID id = UUID.randomUUID();

        when(clienteRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> clienteService.deletar(id));
    }
}
