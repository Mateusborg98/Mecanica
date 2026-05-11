package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.AddServicoPecaOrdemDeServicoDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.PecaRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.ServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.TempoMedioServicoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Operador;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.entity.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.StatusServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.OperadorRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.ServicoRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.security.UsuarioAutenticadoService;
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
        private ServicoRepository servicoRepository;

        @Mock
        private OperadorRepository operadorRepository;

        @Mock
        private PecaServiceImpl pecaService;

        @Mock
        private UsuarioAutenticadoService autenticadoService;

        @Mock
        private OrdemDeServicoMapper mapper;

        private UUID id;
        private OrdemDeServico os;

        @BeforeEach
        void setup() {

                id = UUID.randomUUID();

                os = new OrdemDeServico();
                os.setId(id);
                os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);
                os.setPecas(new ArrayList<>());
                os.setServicos(new ArrayList<>());
        }

        @Test
        void deveCriarOrdemServico() {

                CreateOrdemDeServicoRequestDto request = new CreateOrdemDeServicoRequestDto();

                request.setCpfCnpj("12345678900");
                request.setPlaca("ABC1234");

                Cliente cliente = new Cliente();
                Veiculo veiculo = new Veiculo();
                Operador operador = new Operador();

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(clienteRepository.findByCpfCnpj(any()))
                                .thenReturn(Optional.of(cliente));

                when(veiculoRepository.findByPlaca(any()))
                                .thenReturn(Optional.of(veiculo));

                when(autenticadoService.getMatricula())
                                .thenReturn(1);

                when(operadorRepository.findByMatricula(any()))
                                .thenReturn(Optional.of(operador));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.criar(request);

                assertNotNull(resultado);

                verify(ordemRepository).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoClienteNaoEncontrado() {

                CreateOrdemDeServicoRequestDto request = new CreateOrdemDeServicoRequestDto();

                request.setCpfCnpj("123");

                when(clienteRepository.findByCpfCnpj(any()))
                                .thenReturn(Optional.empty());

                assertThrows(
                                RegraNegocioException.class,
                                () -> service.criar(request));
        }

        @Test
        void deveIniciarDiagnostico() {

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.iniciarDiagnostico(id);

                assertNotNull(resultado);

                assertEquals(
                                StatusOrdemDeServicoEnum.EM_DIAGNOSTICO,
                                os.getStatus());
        }

        @Test
        void deveLancarExcecaoAoIniciarDiagnosticoComStatusInvalido() {

                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                assertThrows(
                                RegraNegocioException.class,
                                () -> service.iniciarDiagnostico(id));
        }

        @Test
        void deveAprovarOrcamento() {

                os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

                ServicoOrdemDeServico servico = new ServicoOrdemDeServico();

                servico.setStatus(StatusServicoEnum.AGUARDANDO);

                os.setServicos(List.of(servico));

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.aprovarOrcamento(id);

                assertNotNull(resultado);

                assertEquals(
                                StatusOrdemDeServicoEnum.EM_EXECUCAO,
                                os.getStatus());

                assertEquals(
                                StatusServicoEnum.EM_EXECUCAO,
                                servico.getStatus());

                assertNotNull(servico.getDtInicio());
        }

        @Test
        void deveNegarOrcamento() {

                os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

                ServicoOrdemDeServico servico = new ServicoOrdemDeServico();

                os.setServicos(List.of(servico));

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.negarOrcamento(id);

                assertNotNull(resultado);

                assertEquals(
                                StatusOrdemDeServicoEnum.EM_DIAGNOSTICO,
                                os.getStatus());

                assertEquals(
                                StatusServicoEnum.CANCELADO,
                                servico.getStatus());

                assertNotNull(servico.getDtFim());
        }

        @Test
        void deveFinalizarOrdemServico() {

                os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);

                ServicoOrdemDeServico servico = new ServicoOrdemDeServico();

                os.setServicos(List.of(servico));

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.finalizar(id);

                assertNotNull(resultado);

                assertEquals(
                                StatusOrdemDeServicoEnum.FINALIZADA,
                                os.getStatus());

                assertEquals(
                                StatusServicoEnum.FINALIZADO,
                                servico.getStatus());

                assertNotNull(servico.getDtFim());
        }

        @Test
        void deveEntregarOrdemServico() {

                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.entregar(id);

                assertNotNull(resultado);

                assertEquals(
                                StatusOrdemDeServicoEnum.ENTREGUE,
                                os.getStatus());
        }

        @Test
        void deveBuscarPorId() {

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                OrdemDeServicoResponseDto resultado = service.buscarPorId(id);

                assertNotNull(resultado);
        }

        @Test
        void deveListarOrdensServico() {

                OrdemDeServicoResponseDto response = new OrdemDeServicoResponseDto();

                when(ordemRepository.findAll())
                                .thenReturn(List.of(os));

                when(mapper.toResponse(any()))
                                .thenReturn(response);

                List<OrdemDeServicoResponseDto> resultado = service.listar();

                assertEquals(1, resultado.size());
        }

        @Test
        void deveCalcularTempoMedioServicos() {

                OrdemDeServico ordem = new OrdemDeServico();

                ordem.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);

                Servico servico = new Servico();
                servico.setDescricao("Troca de óleo");

                ServicoOrdemDeServico sos = new ServicoOrdemDeServico();

                sos.setServico(servico);

                sos.setDtInicio(
                                LocalDateTime.now().minusMinutes(60));

                sos.setDtFim(
                                LocalDateTime.now());

                ordem.setServicos(List.of(sos));

                when(ordemRepository.findByStatus(
                                StatusOrdemDeServicoEnum.FINALIZADA))
                                .thenReturn(List.of(ordem));

                List<TempoMedioServicoResponseDto> resultado = service.calcularTempoMedioServicos();

                assertEquals(1, resultado.size());

                assertEquals(
                                "Troca de óleo",
                                resultado.get(0).getServico());

                assertTrue(
                                resultado.get(0)
                                                .getTempoMedioEmMinutos() > 0);
        }

        @Test
        void deveRetornarListaVaziaQuandoNaoExistirServicoFinalizado() {

                when(ordemRepository.findByStatus(
                                StatusOrdemDeServicoEnum.FINALIZADA))
                                .thenReturn(List.of());

                List<TempoMedioServicoResponseDto> resultado = service.calcularTempoMedioServicos();

                assertTrue(resultado.isEmpty());
        }

        @Test
        void deveLancarExcecaoQuandoOsNaoEncontrada() {

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(
                                RegraNegocioException.class,
                                () -> service.buscarPorId(id));
        }

        @Test
        void naoDeveSalvarQuandoStatusInvalido() {

                os.setStatus(StatusOrdemDeServicoEnum.ENTREGUE);

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                assertThrows(
                                RegraNegocioException.class,
                                () -> service.finalizar(id));

                verify(ordemRepository, never())
                                .save(any());
        }

        @Test
        void deveAdicionarServicoEPeca() {

                UUID pecaId = UUID.randomUUID();
                UUID servicoId = UUID.randomUUID();

                AddServicoPecaOrdemDeServicoDto request = new AddServicoPecaOrdemDeServicoDto();

                PecaRequestDto pecaDto = new PecaRequestDto();
                pecaDto.setPecaId(pecaId);
                pecaDto.setQuantidade(1);

                ServicoRequestDto servicoDto = new ServicoRequestDto();
                servicoDto.setServicoId(servicoId);

                request.setPecas(List.of(pecaDto));
                request.setServicos(List.of(servicoDto));

                Peca peca = new Peca();
                peca.setPreco(BigDecimal.TEN);

                Servico servico = new Servico();
                servico.setPreco(BigDecimal.valueOf(50));

                when(ordemRepository.findById(id))
                                .thenReturn(Optional.of(os));

                when(pecaRepository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(servicoRepository.findById(servicoId))
                                .thenReturn(Optional.of(servico));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(new OrdemDeServicoResponseDto());

                OrdemDeServicoResponseDto resultado = service.adicionarServicoPeca(id, request);

                assertNotNull(resultado);
        }
}