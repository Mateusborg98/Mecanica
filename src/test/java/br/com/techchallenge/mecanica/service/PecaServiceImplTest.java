package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
        private PecaRepository pecaRepository;

        @Mock
        private EstoqueRepository estoqueRepository;

        @Mock
        private PecaMapper mapper;

        @InjectMocks
        private PecaServiceImpl pecaService;

        @BeforeEach
        void setup() {
                pecaService = new PecaServiceImpl(
                                pecaRepository,
                                estoqueRepository,
                                mapper);
        }

        @Test
        void deveCriarPecaComSucesso() {

                CreatePecaRequestDto request = new CreatePecaRequestDto("Filtro", "Bosch", new BigDecimal("50.00"));

                Peca peca = new Peca();

                when(mapper.toEntity(any()))
                                .thenReturn(peca);

                when(pecaRepository.save(any(Peca.class)))
                                .thenReturn(peca);

                pecaService.criar(request);

                verify(pecaRepository).save(any(Peca.class));
                verifyNoInteractions(estoqueRepository);
        }

        @Test
        void deveCriarEstoqueParaPeca() {

                UUID pecaId = UUID.randomUUID();

                Peca peca = new Peca();

                CreateEstoqueRequestDto request = new CreateEstoqueRequestDto(pecaId, 10);

                when(pecaRepository.findById(pecaId))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.save(any(Estoque.class)))
                                .thenAnswer(inv -> inv.getArgument(0));

                pecaService.criarEstoque(request);

                verify(estoqueRepository).save(any(Estoque.class));
        }

        @Test
        void deveAumentarEstoqueAoRegistrarEntrada() {
                UUID id = UUID.randomUUID();

                Peca peca = new Peca(id, "Filtro", "Bosch", new BigDecimal("50.00"));
                Estoque estoque = new Estoque(UUID.randomUUID(), peca, 5);

                when(pecaRepository.findById(id))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                pecaService.registrarEntradaEstoque(id, 3);

                assertEquals(8, estoque.getQuantidade());
        }

        @Test
        void deveDiminuirEstoqueAoRegistrarSaida() {
                UUID id = UUID.randomUUID();

                Peca peca = new Peca(id, "Filtro", "Bosch", new BigDecimal("50.00"));
                Estoque estoque = new Estoque(UUID.randomUUID(), peca, 10);

                when(pecaRepository.findById(id))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                pecaService.registrarSaidaEstoque(id, 4);

                assertEquals(6, estoque.getQuantidade());
        }

        @Test
        void naoDevePermitirEntradaComQuantidadeInvalida() {
                assertThrows(IllegalArgumentException.class,
                                () -> pecaService.registrarEntradaEstoque(UUID.randomUUID(), 0));
        }

        @Test
        void naoDevePermitirSaidaMaiorQueEstoque() {

                UUID id = UUID.randomUUID();
                Peca peca = new Peca(id, "Filtro", "Bosch", new BigDecimal("50.00"));
                Estoque estoque = new Estoque(UUID.randomUUID(), peca, 1);

                when(pecaRepository.findById(id))
                                .thenReturn(Optional.of(peca));

                when(estoqueRepository.findByPeca(peca))
                                .thenReturn(Optional.of(estoque));

                assertThrows(RegraNegocioException.class,
                                () -> pecaService.registrarSaidaEstoque(id, 5));
        }
}
