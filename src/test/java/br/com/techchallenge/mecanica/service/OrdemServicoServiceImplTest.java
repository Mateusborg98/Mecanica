package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Estoque;
import br.com.techchallenge.mecanica.entity.ItemOrdemDeServico;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.implementation.OrdemDeServicoServiceImpl;
import br.com.techchallenge.mecanica.service.implementation.PecaServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoServiceImplTest {

        @InjectMocks
        private OrdemDeServicoServiceImpl service;

        @Mock
        private OrdemDeServicoRepository ordemRepository;
        @Mock
        private ClienteRepository clienteRepository;
        @Mock
        private VeiculoRepository veiculoRepository;
        @Mock
        private PecaRepository pecaRepository;
        @Mock
        private EstoqueRepository estoqueRepository;
        @Mock
        private OrdemDeServicoMapper mapper;
        @Mock
        private PecaServiceImpl pecaServiceImpl;

        private OrdemDeServico os;
        private Cliente cliente;
        private Veiculo veiculo;

        @BeforeEach
        void setup() {
                cliente = new Cliente();
                cliente.setId(UUID.randomUUID());

                veiculo = new Veiculo();
                veiculo.setId(UUID.randomUUID());

                os = new OrdemDeServico();
                os.setId(UUID.randomUUID());
                os.setCliente(cliente);
                os.setVeiculo(veiculo);
                os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);
                os.setDtInicioOs(LocalDateTime.now());

        }

        // ---------------- CRIAR ----------------

        @Test
        void deveCriarOrdemDeServico() {
                CreateOrdemDeServicoRequestDto dto = mock(CreateOrdemDeServicoRequestDto.class);
                when(dto.getClienteId()).thenReturn(cliente.getId());
                when(dto.getVeiculoId()).thenReturn(veiculo.getId());

                when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
                when(veiculoRepository.findById(veiculo.getId())).thenReturn(Optional.of(veiculo));

                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());

                OrdemDeServicoResponseDto response = service.criar(dto);

                assertNotNull(response);
                verify(ordemRepository).save(any());
        }

        @Test
        void deveLancarErroAoCriarComClienteInexistente() {
                CreateOrdemDeServicoRequestDto dto = mock(CreateOrdemDeServicoRequestDto.class);
                when(dto.getClienteId()).thenReturn(UUID.randomUUID());

                when(clienteRepository.findById(any())).thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class, () -> service.criar(dto));
        }

        // ---------------- ADICIONAR PEÇA ----------------

        @Test
        void deveAdicionarPecaNaOs() {
                Peca peca = new Peca();
                peca.setId(UUID.randomUUID());
                peca.setPreco(BigDecimal.TEN);

                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));
                when(pecaRepository.findById(peca.getId())).thenReturn(Optional.of(peca));

                assertDoesNotThrow(() -> service.adicionarPecaNaOs(os.getId(), peca.getId(), 2));
        }

        // ---------------- STATUS ----------------

        @Test
        void deveIniciarDiagnostico() {
                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());
                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));

                OrdemDeServicoResponseDto response = service.iniciarDiagnostico(os.getId());

                assertNotNull(response);
                assertEquals(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO, os.getStatus());
        }

        @Test
        void deveEnviarOrcamento() {
                os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));
                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());

                OrdemDeServicoResponseDto response = service.enviarOrcamento(os.getId());

                assertNotNull(response);
                assertEquals(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO, os.getStatus());
                assertNotNull(os.getValorTotalOs());
        }

        @Test
        void deveAprovarOrcamento() {
                os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

                ItemOrdemDeServico item = mock(ItemOrdemDeServico.class);
                Peca peca = mock(Peca.class);
                Estoque estoque = mock(Estoque.class);

                when(item.getQuantidade()).thenReturn(1);
                when(item.getPeca()).thenReturn(peca);
                when(estoque.getQuantidade()).thenReturn(5);

                os.getItens().add(item);

                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.of(estoque));
                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());

                OrdemDeServicoResponseDto response = service.aprovarOrcamento(os.getId());

                assertEquals(StatusOrdemDeServicoEnum.EM_EXECUCAO, os.getStatus());
                assertNotNull(response);
        }

        // ---------------- FINALIZAR / ENTREGAR ----------------

        @Test
        void deveFinalizarOrdem() {
                os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);
                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));

                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());

                OrdemDeServicoResponseDto response = service.finalizar(os.getId());

                assertEquals(StatusOrdemDeServicoEnum.FINALIZADA, os.getStatus());
                assertNotNull(os.getDtFimOs());
                assertNotNull(response);
        }

        @Test
        void deveEntregarOrdem() {
                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());
                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);
                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));

                OrdemDeServicoResponseDto response = service.entregar(os.getId());

                assertEquals(StatusOrdemDeServicoEnum.ENTREGUE, os.getStatus());
                assertNotNull(response);
        }

        // ---------------- BUSCAR / LISTAR ----------------

        @Test
        void deveBuscarPorId() {
                when(ordemRepository.findById(os.getId())).thenReturn(Optional.of(os));
                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());
                assertNotNull(service.buscarPorId(os.getId()));
        }

        @Test
        void deveListarOrdens() {
                when(ordemRepository.findAll()).thenReturn(List.of(os));

                assertEquals(1, service.listar().size());
        }

        // ---------------- TEMPO MÉDIO ----------------

        @Test
        void deveCalcularTempoMedioExecucao() {
                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);
                os.setDtInicioOs(LocalDateTime.now().minusHours(4));
                os.setDtFimOs(LocalDateTime.now());

                when(ordemRepository.findByStatus(StatusOrdemDeServicoEnum.FINALIZADA))
                                .thenReturn(List.of(os));

                Duration resultado = service.calcularTempoMedioExecucao();

                assertEquals(4, resultado.toHours());
        }

        @Test
        void deveRetornarZeroQuandoNaoHaFinalizadas() {
                when(ordemRepository.findByStatus(StatusOrdemDeServicoEnum.FINALIZADA))
                                .thenReturn(List.of());

                Duration duration = service.calcularTempoMedioExecucao();

                assertEquals(Duration.ZERO, duration);
        }
}