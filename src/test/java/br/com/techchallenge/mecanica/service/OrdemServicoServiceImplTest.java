package br.com.techchallenge.mecanica.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.mapper.PecaMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.implementation.OrdemDeServicoServiceImpl;
import br.com.techchallenge.mecanica.service.implementation.PecaServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceImplTest {

        @Mock
        OrdemDeServicoRepository ordemRepository;

        @Mock
        ClienteRepository clienteRepository;

        @Mock
        VeiculoRepository veiculoRepository;

        @Mock
        PecaRepository pecaRepository;

        @Mock
        EstoqueRepository estoqueRepository;

        @Mock
        OrdemDeServicoMapper mapper;

        PecaServiceImpl pecaService;
        OrdemDeServicoServiceImpl ordemService;

        @BeforeEach
        void setup() {
                pecaService = new PecaServiceImpl(
                                pecaRepository,
                                estoqueRepository,
                                mock(PecaMapper.class));

                ordemService = new OrdemDeServicoServiceImpl(
                                pecaService, ordemRepository,
                                clienteRepository,
                                veiculoRepository,
                                pecaRepository,
                                estoqueRepository,
                                mapper);
        }

        @Test
        void aoCriarOS_deveIniciarComStatusRecebida() {
                Cliente cliente = new Cliente();
                Veiculo veiculo = new Veiculo();

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.of(cliente));
                when(veiculoRepository.findById(any()))
                                .thenReturn(Optional.of(veiculo));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                CreateOrdemDeServicoRequestDto request = mock(CreateOrdemDeServicoRequestDto.class);
                when(request.getClienteId()).thenReturn(UUID.randomUUID());
                when(request.getVeiculoId()).thenReturn(UUID.randomUUID());

                ordemService.criar(request);
        }
}