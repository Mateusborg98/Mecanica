package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import jakarta.persistence.EntityNotFoundException;

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
        private PecaResponseDto responseDto;

        @BeforeEach
        void setup() {
                pecaId = UUID.randomUUID();
                peca = new Peca();
                responseDto = new PecaResponseDto();
        }

        // =========================
        // criar
        // =========================
        @Test
        void deveCriarPecaComSucesso() {
                CreatePecaRequestDto request = new CreatePecaRequestDto("Bateria", "Moura", new BigDecimal("500"));

                when(mapper.toEntity(request)).thenReturn(peca);
                when(repository.save(peca)).thenReturn(peca);
                when(mapper.toResponseDto(peca)).thenReturn(responseDto);

                PecaResponseDto resultado = service.criar(request);

                assertNotNull(resultado);
                verify(repository).save(peca);
        }

        // =========================
        // registrarEntradaEstoque
        // =========================
        @Test
        void deveRegistrarEntradaEstoqueComSucesso() {
                Estoque estoque = mock(Estoque.class);

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.of(estoque));

                service.registrarEntradaEstoque(pecaId, 10);

                verify(estoque).registrarEntrada(10);
                verify(estoqueRepository).save(estoque);
        }

        @Test
        void deveFalharEntradaEstoqueQuandoPecaNaoExiste() {
                when(repository.findById(pecaId)).thenReturn(Optional.empty());

                assertThrows(IllegalArgumentException.class,
                                () -> service.registrarEntradaEstoque(pecaId, 10));
        }

        // =========================
        // registrarSaidaEstoque
        // =========================
        @Test
        void deveRegistrarSaidaEstoqueComSucesso() {
                Estoque estoque = mock(Estoque.class);

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.of(estoque));

                service.registrarSaidaEstoque(pecaId, 5);

                verify(estoque).registrarSaida(5);
                verify(estoqueRepository).save(estoque);
        }

        @Test
        void deveFalharSaidaEstoqueQuandoEstoqueNaoExiste() {
                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarSaidaEstoque(pecaId, 5));
        }

        // =========================
        // buscarPorId
        // =========================
        @Test
        void deveBuscarPecaPorId() {
                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(mapper.toResponseDto(peca)).thenReturn(responseDto);

                PecaResponseDto resultado = service.buscarPorId(pecaId);

                assertNotNull(resultado);
        }

        @Test
        void deveLancarExcecaoQuandoBuscarPecaInexistente() {
                when(repository.findById(pecaId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class,
                                () -> service.buscarPorId(pecaId));
        }

        // =========================
        // atualizar
        // =========================
        @Test
        void deveAtualizarPecaComSucesso() {
                UpdatePecaRequestDto request = new UpdatePecaRequestDto();

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(mapper.toResponseDto(peca)).thenReturn(responseDto);

                PecaResponseDto resultado = service.atualizar(pecaId, request);

                verify(mapper).updateEntity(request, peca);
                assertNotNull(resultado);
        }

        // =========================
        // listar
        // =========================
        @Test
        void deveListarPecas() {
                when(repository.findAll()).thenReturn(List.of(peca));
                when(mapper.toResponseDto(peca)).thenReturn(responseDto);

                List<PecaResponseDto> lista = service.listar();

                assertEquals(1, lista.size());
        }

        // =========================
        // deletar
        // =========================
        @Test
        void deveDeletarPeca() {
                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));

                service.deletar(pecaId);

                verify(repository).delete(peca);
        }

        // =========================
        // criarEstoque
        // =========================
        @Test
        void deveCriarEstoqueComSucesso() {
                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto(pecaId, 10);

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.empty());

                service.criarEstoque(request);

                verify(estoqueRepository).save(any(Estoque.class));
        }

        @Test
        void deveFalharAoCriarEstoqueComQuantidadeNegativa() {
                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto(pecaId, -1);

                assertThrows(IllegalArgumentException.class,
                                () -> service.criarEstoque(request));
        }

        @Test
        void deveFalharAoCriarEstoqueDuplicado() {
                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto(pecaId, 5);

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.of(new Estoque()));

                assertThrows(RegraNegocioException.class,
                                () -> service.criarEstoque(request));
        }

        @Test
        void deveFalharEntradaEstoqueQuandoEstoqueNaoExiste() {

                when(repository.findById(pecaId)).thenReturn(Optional.of(peca));
                when(estoqueRepository.findByPeca(peca)).thenReturn(Optional.empty());

                assertThrows(IllegalArgumentException.class,
                                () -> service.registrarEntradaEstoque(pecaId, 10));
        }

        @Test
        void deveFalharSaidaEstoqueQuandoPecaNaoExiste() {

                when(repository.findById(pecaId)).thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.registrarSaidaEstoque(pecaId, 5));
        }

        @Test
        void deveFalharAoAtualizarQuandoPecaNaoExiste() {

                UpdatePecaRequestDto request = new UpdatePecaRequestDto();

                when(repository.findById(pecaId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class,
                                () -> service.atualizar(pecaId, request));
        }

        @Test
        void deveFalharAoCriarEstoqueQuandoPecaNaoExiste() {

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto(pecaId, 10);

                when(repository.findById(pecaId)).thenReturn(Optional.empty());

                assertThrows(RegraNegocioException.class,
                                () -> service.criarEstoque(request));
        }

}