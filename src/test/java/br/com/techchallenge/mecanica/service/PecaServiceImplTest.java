package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.estoqueDto.CreateEstoqueRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.dto.pecaDto.UpdatePecaRequestDto;
import br.com.techchallenge.mecanica.entity.Estoque;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.PecaMapper;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.service.implementation.PecaServiceImpl;

@ExtendWith(MockitoExtension.class)
class PecaServiceImplTest {

        @Mock
        private PecaRepository repository;

        @Mock
        private EstoqueRepository estoqueRepository;

        @Mock
        private PecaMapper mapper;

        @InjectMocks
        private PecaServiceImpl service;

        private UUID pecaId;
        private Peca peca;

        @BeforeEach
        void setup() {

                pecaId = UUID.randomUUID();

                peca = new Peca();
                peca.setId(pecaId);
                peca.setNome("Filtro de Óleo");
                peca.setMarca("Bosch");
                peca.setPreco(new BigDecimal("50.00"));
        }

        @Test
        void deveCriarPeca() {

                CreatePecaRequestDto request = new CreatePecaRequestDto();
                request.setNome("Filtro");

                PecaResponseDto responseDto = new PecaResponseDto();

                when(mapper.toEntity(request))
                                .thenReturn(peca);

                when(repository.save(peca))
                                .thenReturn(peca);

                when(mapper.toResponseDto(peca))
                                .thenReturn(responseDto);

                PecaResponseDto response = service.criar(request);

                assertNotNull(response);

                verify(repository).save(peca);
        }

        @Test
        void deveRegistrarEntradaEstoque() {

                Estoque estoque = new Estoque();
                estoque.setQuantidade(10);

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                assertDoesNotThrow(() -> service.registrarEntradaEstoque(pecaId, 5));

                assertEquals(15, estoque.getQuantidade());

                verify(estoqueRepository).save(estoque);
        }

        @Test
        void deveLancarExcecaoAoRegistrarEntradaQuandoPecaNaoExiste() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarEntradaEstoque(pecaId, 5));
        }

        @Test
        void deveLancarExcecaoAoRegistrarEntradaQuandoEstoqueNaoExiste() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarEntradaEstoque(pecaId, 5));
        }

        @Test
        void deveRegistrarSaidaEstoque() {

                Estoque estoque = new Estoque();
                estoque.setQuantidade(20);

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                assertDoesNotThrow(() -> service.registrarSaidaEstoque(pecaId, 5));

                assertEquals(15, estoque.getQuantidade());

                verify(estoqueRepository).save(estoque);
        }

        @Test
        void deveLancarExcecaoAoRegistrarSaidaQuandoPecaNaoExiste() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarSaidaEstoque(pecaId, 5));
        }

        @Test
        void deveLancarExcecaoAoRegistrarSaidaQuandoEstoqueNaoExiste() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarSaidaEstoque(pecaId, 5));
        }

        @Test
        void deveBuscarPecaPorId() {

                PecaResponseDto responseDto = new PecaResponseDto();

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(mapper.toResponseDto(peca))
                                .thenReturn(responseDto);

                PecaResponseDto response = service.buscarPorId(pecaId);

                assertNotNull(response);
        }

        @Test
        void deveLancarExcecaoAoBuscarPecaPorId() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.buscarPorId(pecaId));
        }

        @Test
        void deveAtualizarPeca() {

                UpdatePecaRequestDto request = new UpdatePecaRequestDto();
                request.setNome("Novo Nome");

                PecaResponseDto responseDto = new PecaResponseDto();

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(mapper.toResponseDto(peca))
                                .thenReturn(responseDto);

                PecaResponseDto response = service.atualizar(pecaId, request);

                assertNotNull(response);

                verify(mapper).updateEntity(request, peca);
        }

        @Test
        void deveLancarExcecaoAoAtualizarPeca() {

                UpdatePecaRequestDto request = new UpdatePecaRequestDto();

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.atualizar(pecaId, request));
        }

        @Test
        void deveListarPecas() {

                PecaResponseDto responseDto = new PecaResponseDto();

                when(repository.findAll())
                                .thenReturn(List.of(peca));

                when(mapper.toResponseDto(peca))
                                .thenReturn(responseDto);

                List<PecaResponseDto> response = service.listar();

                assertEquals(1, response.size());
        }

        @Test
        void deveDeletarPeca() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                service.deletar(pecaId);

                verify(repository).delete(peca);
        }

        @Test
        void deveLancarExcecaoAoDeletarPeca() {

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.deletar(pecaId));

                verify(repository, never()).delete(any());
        }

        @Test
        void deveCriarEstoque() {

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto();

                request.setPeca(peca);
                request.setQuantidade(10);

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.empty());

                assertDoesNotThrow(() -> service.criarEstoque(peca, request.getQuantidade()));

                verify(estoqueRepository).save(any(Estoque.class));
        }

        @Test
        void deveLancarExcecaoQuandoQuantidadeInicialForNegativa() {

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto();

                request.setQuantidade(-1);

                assertThrows(RegraNegocioException.class,
                                () -> service.criarEstoque(peca, request.getQuantidade()));

                verify(estoqueRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoPecaNaoEncontradaAoCriarEstoque() {

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto();

                request.setPeca(peca);
                request.setQuantidade(10);

                when(repository.findById(pecaId))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.criarEstoque(peca, request.getQuantidade()));
        }

        @Test
        void deveLancarExcecaoQuandoJaExisteEstoque() {

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto();

                request.setPeca(peca);
                request.setQuantidade(10);

                Estoque estoque = new Estoque();

                when(repository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                assertThrows(RegraNegocioException.class,
                                () -> service.criarEstoque(peca, request.getQuantidade()));
        }

        @Test
        void deveLancarExcecaoAoDeletarQuandoPecaNaoEncontrada() {

                UUID id = UUID.randomUUID();

                when(repository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.deletar(id));
        }
}