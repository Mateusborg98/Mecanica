package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
        private ClienteRepository repository;

        @InjectMocks
        private ClienteServiceImpl service;

        @Test
        void deveCriarCliente() {

                CreateClienteRequestDto request = new CreateClienteRequestDto();

                request.setNome("João");
                request.setCpfCnpj("12345678900");

                Cliente cliente = new Cliente();

                when(repository.existsByCpfCnpj(any()))
                                .thenReturn(false);

                when(repository.save(any()))
                                .thenReturn(cliente);

                ClienteResponseDto response = service.criar(request);

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoQuandoCpfJaExistir() {

                CreateClienteRequestDto request = new CreateClienteRequestDto();

                request.setCpfCnpj("123");

                when(repository.existsByCpfCnpj(any()))
                                .thenReturn(true);

                assertThrows(RegraNegocioException.class,
                                () -> service.criar(request));
        }

        @Test
        void deveBuscarPorId() {

                UUID id = UUID.randomUUID();

                Cliente cliente = new Cliente();

                when(repository.findById(id))
                                .thenReturn(Optional.of(cliente));

                ClienteResponseDto response = service.buscarPorId(id);

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoAoBuscarPorId() {

                UUID id = UUID.randomUUID();

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.buscarPorId(id));
        }

        @Test
        void deveListarClientes() {

                when(repository.findAll())
                                .thenReturn(List.of(new Cliente()));

                List<ClienteResponseDto> response = service.listar();

                assertEquals(1, response.size());
        }

        @Test
        void deveAtualizarCliente() {

                UUID id = UUID.randomUUID();

                Cliente cliente = new Cliente();

                UpdateClienteRequestDto request = new UpdateClienteRequestDto();

                request.setNome("Novo Nome");

                when(repository.findById(id))
                                .thenReturn(Optional.of(cliente));

                ClienteResponseDto response = service.atualizar(id, request);

                assertEquals("Novo Nome",
                                response.getNome());
        }

        @Test
        void deveDeletarCliente() {

                UUID id = UUID.randomUUID();

                Cliente cliente = new Cliente();

                when(repository.findById(id))
                                .thenReturn(Optional.of(cliente));

                service.deletar(id);

                verify(repository).delete(cliente);
        }

        @Test
        void deveBuscarPorCpfCnpj() {

                Cliente cliente = new Cliente();

                when(repository.findByCpfCnpj("123"))
                                .thenReturn(Optional.of(cliente));

                ClienteResponseDto response = service.buscarPorCpfCnpj("123");

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoBuscarPorCpfCnpj() {

                when(repository.findByCpfCnpj("123"))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.buscarPorCpfCnpj("123"));
        }
}