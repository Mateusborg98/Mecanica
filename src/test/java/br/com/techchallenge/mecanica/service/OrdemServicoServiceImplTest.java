package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import br.com.techchallenge.mecanica.entity.Servico;
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
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoServiceImplTest {

        @Mock
        private PecaServiceImpl pecaServiceImpl;

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

        @InjectMocks
        private OrdemDeServicoServiceImpl service;

        private CreateOrdemDeServicoRequestDto createOrdemDeServicoRequestDtoHelper() {
                OrdemDeServico os = new OrdemDeServico();
                Servico servico = new Servico(UUID.randomUUID(), "Troca de óleo", new BigDecimal("20"), os);
                List<Servico> servicos = new ArrayList<>();
                servicos.add(servico);
                os.setServicos(servicos);

                Peca peca = new Peca(UUID.randomUUID(), "Óleo", "Petronas", new BigDecimal("40"));
                ItemOrdemDeServico itemOrdemDeServico = new ItemOrdemDeServico(UUID.randomUUID(), os, peca, 4,
                                new BigDecimal("40"));
                List<ItemOrdemDeServico> itemOrdemDeServicos = new ArrayList<>();
                itemOrdemDeServicos.add(itemOrdemDeServico);

                CreateOrdemDeServicoRequestDto dto = new CreateOrdemDeServicoRequestDto(UUID.randomUUID(),
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                itemOrdemDeServicos, servicos);
                return dto;

        }

        // ================================
        // CREATE
        // ================================

        @Test
        void deveCriarOrdemDeServicoComSucesso() {

                Cliente cliente = new Cliente();
                Veiculo veiculo = new Veiculo();

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.of(cliente));
                when(veiculoRepository.findById(any()))
                                .thenReturn(Optional.of(veiculo));
                when(ordemRepository.save(any()))
                                .thenAnswer(inv -> inv.getArgument(0));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.criar(createOrdemDeServicoRequestDtoHelper());

                assertNotNull(response);
        }

        @Test
        void naoDeveCriarOrdemDeServicoComClienteInexistente() {

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.criar(createOrdemDeServicoRequestDtoHelper()));
        }

        @Test
        void naoDeveCriarOrdemDeServicoComVeiculoInexistente() {

                when(clienteRepository.findById(any()))
                                .thenReturn(Optional.of(new Cliente()));
                when(veiculoRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.criar(createOrdemDeServicoRequestDtoHelper()));
        }

        // ================================
        // ADICIONAR PEÇA
        // ================================

        @Test
        void deveAdicionarPecaNaOsComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                Peca peca = new Peca();

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(pecaRepository.findById(any()))
                                .thenReturn(Optional.of(peca));
                doNothing().when(pecaServiceImpl)
                                .registrarSaidaEstoque(any(), anyInt());
                when(ordemRepository.save(any()))
                                .thenReturn(os);
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.adicionarPecaNaOs(
                                UUID.randomUUID(), UUID.randomUUID(), 2);

                assertNotNull(response);
        }

        @Test
        void naoDeveAdicionarPecaSeOsNaoExistir() {

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.adicionarPecaNaOs(
                                                UUID.randomUUID(), UUID.randomUUID(), 1));
        }

        // ================================
        // STATUS FLOW
        // ================================

        @Test
        void deveIniciarDiagnosticoComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(ordemRepository.save(any()))
                                .thenReturn(os);
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.iniciarDiagnostico(UUID.randomUUID());

                assertNotNull(response);
        }

        @Test
        void naoDeveIniciarDiagnosticoComStatusInvalido() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));

                assertThrows(RegraNegocioException.class,
                                () -> service.iniciarDiagnostico(UUID.randomUUID()));
        }

        @Test
        void deveAprovarOrcamentoComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.aprovarOrcamento(UUID.randomUUID());

                assertNotNull(response);
        }

        @Test
        void deveIniciarExecucaoComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.ORCAMENTO_APROVADO);

                ItemOrdemDeServico item = new ItemOrdemDeServico();
                Peca peca = new Peca();
                item.setPeca(peca);
                item.setQuantidade(2);
                os.setItens(List.of(item));

                Estoque estoque = new Estoque();
                estoque.setQuantidade(10);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(estoqueRepository.findByPeca(any()))
                                .thenReturn(Optional.of(estoque));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.iniciarExecucao(UUID.randomUUID());

                assertNotNull(response);
        }

        @Test
        void naoDeveIniciarExecucaoComEstoqueInsuficiente() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.ORCAMENTO_APROVADO);

                ItemOrdemDeServico item = new ItemOrdemDeServico();
                Peca peca = new Peca();
                item.setPeca(peca);
                item.setQuantidade(5);
                os.setItens(List.of(item));

                Estoque estoque = new Estoque();
                estoque.setQuantidade(1);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(estoqueRepository.findByPeca(any()))
                                .thenReturn(Optional.of(estoque));

                assertThrows(RegraNegocioException.class,
                                () -> service.iniciarExecucao(UUID.randomUUID()));
        }

        // ================================
        // FINALIZAR / ENTREGAR
        // ================================

        @Test
        void deveFinalizarComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(ordemRepository.save(any()))
                                .thenReturn(os);
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.finalizar(UUID.randomUUID());

                assertNotNull(response);
        }

        @Test
        void deveEntregarComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(ordemRepository.save(any()))
                                .thenReturn(os);
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.entregar(UUID.randomUUID());

                assertNotNull(response);
        }

        // ================================
        // BUSCA / LISTA
        // ================================

        @Test
        void deveBuscarPorIdComSucesso() {

                OrdemDeServico os = new OrdemDeServico();

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                assertNotNull(service.buscarPorId(UUID.randomUUID()));
        }

        @Test
        void naoDeveBuscarPorIdInexistente() {

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class,
                                () -> service.buscarPorId(UUID.randomUUID()));
        }

        @Test
        void deveListarOrdens() {

                when(ordemRepository.findAll())
                                .thenReturn(List.of(new OrdemDeServico()));
                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                List<OrdemDeServicoResponseDto> lista = service.listar();

                assertFalse(lista.isEmpty());
        }

        @Test
        void naoDeveIniciarExecucaoQuandoEstoqueNaoEncontrado() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.ORCAMENTO_APROVADO);

                ItemOrdemDeServico item = new ItemOrdemDeServico();
                Peca peca = new Peca();
                peca.setNome("Filtro de óleo");

                item.setPeca(peca);
                item.setQuantidade(1);
                os.setItens(List.of(item));

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));

                when(estoqueRepository.findByPeca(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.iniciarExecucao(UUID.randomUUID()));
        }

        @Test
        void naoDeveEnviarParaAprovacaoQuandoOsNaoExiste() {

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.enviarParaAprovacao(UUID.randomUUID()));
        }

        @Test
        void deveEnviarParaAprovacaoComSucesso() {

                OrdemDeServico os = new OrdemDeServico();
                os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));

                when(ordemRepository.save(any()))
                                .thenReturn(os);

                when(mapper.toResponse(any()))
                                .thenReturn(mock(OrdemDeServicoResponseDto.class));

                OrdemDeServicoResponseDto response = service.enviarParaAprovacao(UUID.randomUUID());

                assertNotNull(response);
        }

        @Test
        void naoDeveAdicionarPecaQuandoPecaNaoExiste() {

                OrdemDeServico os = new OrdemDeServico();

                when(ordemRepository.findById(any()))
                                .thenReturn(Optional.of(os));

                when(pecaRepository.findById(any()))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.adicionarPecaNaOs(
                                                UUID.randomUUID(), UUID.randomUUID(), 1));
        }

}
