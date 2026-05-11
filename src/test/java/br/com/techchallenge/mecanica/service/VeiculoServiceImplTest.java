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

import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.implementation.VeiculoServiceImpl;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceImplTest {

        @Mock
        private VeiculoRepository repository;

        @Mock
        private ClienteRepository clienteRepository;

        @InjectMocks
        private VeiculoServiceImpl service;

        private Veiculo criarVeiculoMock() {

                Cliente cliente = new Cliente();
                cliente.setId(UUID.randomUUID());
                cliente.setNome("João");

                Veiculo veiculo = new Veiculo();
                veiculo.setId(UUID.randomUUID());
                veiculo.setPlaca("ABC1D23");
                veiculo.setMarca("Honda");
                veiculo.setModelo("Civic");
                veiculo.setAno(2020);

                // ESSENCIAL
                veiculo.setCliente(cliente);

                return veiculo;
        }

        @Test
        void deveCriarVeiculo() {

                UUID clienteId = UUID.randomUUID();

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto();
                request.setClienteId(clienteId);
                request.setPlaca("ABC1D23");

                Cliente cliente = new Cliente();
                cliente.setId(clienteId);

                Veiculo veiculo = criarVeiculoMock();

                when(repository.existsByPlaca(any()))
                                .thenReturn(false);

                when(clienteRepository.findById(clienteId))
                                .thenReturn(Optional.of(cliente));

                when(repository.save(any()))
                                .thenReturn(veiculo);

                VeiculoResponseDto response = service.criar(request);

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoQuandoPlacaJaExistir() {

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto();

                when(repository.existsByPlaca(any()))
                                .thenReturn(true);

                assertThrows(RegraNegocioException.class,
                                () -> service.criar(request));
        }

        @Test
        void deveLancarExcecaoQuandoClienteNaoEncontrado() {

                UUID clienteId = UUID.randomUUID();

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto();
                request.setClienteId(clienteId);

                when(repository.existsByPlaca(any()))
                                .thenReturn(false);

                when(clienteRepository.findById(clienteId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.criar(request));
        }

        @Test
        void deveBuscarPorId() {

                UUID id = UUID.randomUUID();

                Veiculo veiculo = criarVeiculoMock();

                when(repository.findById(id))
                                .thenReturn(Optional.of(veiculo));

                VeiculoResponseDto response = service.buscarPorId(id);

                assertNotNull(response);
        }

        @Test
        void deveListarVeiculos() {

                Veiculo veiculo = criarVeiculoMock();

                when(repository.findAll())
                                .thenReturn(List.of(veiculo));

                List<VeiculoResponseDto> response = service.listar();

                assertEquals(1, response.size());
        }

        @Test
        void deveAtualizarVeiculo() {

                UUID id = UUID.randomUUID();

                Veiculo veiculo = criarVeiculoMock();

                UpdateVeiculoRequestDto request = new UpdateVeiculoRequestDto();
                request.setModelo("Novo Modelo");

                when(repository.findById(id))
                                .thenReturn(Optional.of(veiculo));

                when(repository.save(any()))
                                .thenReturn(veiculo);

                VeiculoResponseDto response = service.atualizar(id, request);

                assertNotNull(response);
        }

        @Test
        void deveDeletarVeiculo() {

                UUID id = UUID.randomUUID();

                Veiculo veiculo = criarVeiculoMock();

                when(repository.findById(id))
                                .thenReturn(Optional.of(veiculo));

                service.deletar(id);

                verify(repository).delete(veiculo);
        }

        @Test
        void deveBuscarPorPlaca() {

                Veiculo veiculo = criarVeiculoMock();

                when(repository.findByPlaca("ABC1D23"))
                                .thenReturn(Optional.of(veiculo));

                VeiculoResponseDto response = service.buscarPorPlaca("ABC1D23");

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoBuscarPorPlaca() {

                when(repository.findByPlaca(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.buscarPorPlaca("AAA"));
        }
}