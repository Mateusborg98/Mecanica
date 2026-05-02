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

import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.VeiculoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.implementation.VeiculoServiceImpl;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceImplTest {

        @Mock
        private VeiculoRepository veiculoRepository;

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private VeiculoMapper mapper;

        @InjectMocks
        private VeiculoServiceImpl veiculoService;

        @Test
        void deveCriarVeiculoComSucesso() {
                UUID clienteId = UUID.randomUUID();

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto("ABC1D23", "Toyota", "Corolla", 2022,
                                clienteId);

                Cliente cliente = new Cliente();
                cliente.setId(clienteId);

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.of(new Cliente()));

                when(veiculoRepository.existsByPlaca("ABC1D23"))
                                .thenReturn(false);

                when(veiculoRepository.save(any(Veiculo.class)))
                                .thenAnswer(inv -> inv.getArgument(0));

                VeiculoResponseDto response = veiculoService.criar(request);

                assertNotNull(response);
                assertEquals("ABC1D23", response.getPlaca());
                verify(veiculoRepository).save(any(Veiculo.class));
        }

        @Test
        void naoDeveCriarVeiculoComPlacaDuplicada() {
                UUID clienteId = UUID.randomUUID();
                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto("ABC1D23", "Toyota", "Corolla", 2022,
                                clienteId);

                when(veiculoRepository.existsByPlaca("ABC1D23"))
                                .thenReturn(true);

                assertThrows(RegraNegocioException.class,
                                () -> veiculoService.criar(request));

                verify(veiculoRepository, never()).save(any());
        }

        @Test
        void naoDeveCriarVeiculoSemClienteValido() {
                UUID clienteId = UUID.randomUUID();

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto("ABC1D23", "Toyota", "Corolla", 2022,
                                clienteId);
                request.setClienteId(clienteId);

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.empty());

                when(veiculoRepository.existsByPlaca("ABC1D23"))
                                .thenReturn(false);

                assertThrows(RegraNegocioException.class,
                                () -> veiculoService.criar(request));
        }

        @Test
        void naoDeveCriarVeiculoComPlacaInvalida() {
                UUID clienteId = UUID.randomUUID();

                CreateVeiculoRequestDto request = new CreateVeiculoRequestDto("123", "Toyota", "Corolla", 2022,
                                clienteId);

                assertThrows(RegraNegocioException.class,
                                () -> veiculoService.criar(request));
        }

        @Test
        void deveBuscarVeiculoPorPlaca() {

                Cliente cliente = new Cliente();
                cliente.setId(UUID.randomUUID());

                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca("ABC1D23");
                veiculo.setCliente(cliente);

                when(veiculoRepository.findByPlaca("ABC1D23"))
                                .thenReturn(Optional.of(veiculo));

                veiculoService.buscarPorPlaca("ABC1D23");

                verify(veiculoRepository).findByPlaca("ABC1D23");
        }

        @Test
        void deveFalharAoBuscarPlacaInexistente() {
                when(veiculoRepository.findByPlaca("ZZZ9Z99"))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> veiculoService.buscarPorPlaca("ZZZ9Z99"));
        }

        @Test
        void deveAtualizarVeiculoComSucesso() {

                UUID veiculoId = UUID.randomUUID();

                Cliente cliente = new Cliente();
                cliente.setId(UUID.randomUUID());

                Veiculo veiculo = new Veiculo();
                veiculo.setCliente(cliente);

                UpdateVeiculoRequestDto request = new UpdateVeiculoRequestDto("Honda", "Civic", 2023);

                when(veiculoRepository.findById(veiculoId))
                                .thenReturn(Optional.of(veiculo));

                when(veiculoRepository.save(any(Veiculo.class)))
                                .thenReturn(veiculo);

                veiculoService.atualizar(veiculoId, request);

                verify(veiculoRepository).save(any(Veiculo.class));
        }
}
