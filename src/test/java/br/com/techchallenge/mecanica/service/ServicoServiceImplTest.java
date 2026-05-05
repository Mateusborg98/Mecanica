package br.com.techchallenge.mecanica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.techchallenge.mecanica.dto.servicoDto.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.servicoDto.UpdateServicoRequestDTO;
import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.mapper.ServicoMapper;
import br.com.techchallenge.mecanica.repository.ServicoRepository;
import br.com.techchallenge.mecanica.service.implementation.ServicoServiceImpl;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ServicoServiceImplTest {

    @Mock
    private ServicoRepository repository;

    @Mock
    private ServicoMapper mapper;

    @InjectMocks
    private ServicoServiceImpl service;

    private UUID servicoId;
    private Servico servico;
    private ServicoResponseDto responseDto;

    @BeforeEach
    void setup() {
        servicoId = UUID.randomUUID();
        servico = new Servico();
        responseDto = new ServicoResponseDto();
    }

    // =========================
    // criar
    // =========================
    @Test
    void deveCriarServicoComSucesso() {
        CreateServicoRequestDto request = new CreateServicoRequestDto();

        when(mapper.toEntity(request)).thenReturn(servico);
        when(repository.save(any(Servico.class))).thenReturn(servico);
        when(mapper.toResponse(servico)).thenReturn(responseDto);

        ServicoResponseDto resultado = service.criar(request);

        assertNotNull(resultado);
        verify(repository).save(any(Servico.class));
        verify(mapper).toEntity(request);
    }

    // =========================
    // buscarPorId
    // =========================
    @Test
    void deveBuscarServicoPorIdComSucesso() {
        when(repository.findById(servicoId)).thenReturn(Optional.of(servico));
        when(mapper.toResponse(servico)).thenReturn(responseDto);

        ServicoResponseDto resultado = service.buscarPorId(servicoId);

        assertNotNull(resultado);
    }

    @Test
    void deveLancarExcecaoQuandoServicoNaoExiste() {
        when(repository.findById(servicoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(servicoId));
    }

    // =========================
    // listar
    // =========================
    @Test
    void deveListarServicos() {
        when(repository.findAll()).thenReturn(List.of(servico));
        when(mapper.toResponse(servico)).thenReturn(responseDto);

        List<ServicoResponseDto> lista = service.listar();

        assertEquals(1, lista.size());
    }

    // =========================
    // atualizar
    // =========================
    @Test
    void deveAtualizarServicoComSucesso() {
        UpdateServicoRequestDTO request = new UpdateServicoRequestDTO();

        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(servico));

        when(repository.save(any(Servico.class)))
                .thenReturn(servico);

        doNothing().when(mapper)
                .updateEntity(any(UpdateServicoRequestDTO.class), any(Servico.class));

        when(mapper.toResponse(any(Servico.class)))
                .thenReturn(responseDto);

        ServicoResponseDto resultado = service.atualizar(servicoId, request);

        assertNotNull(resultado);

        verify(repository).save(servico);
        verify(mapper).updateEntity(request, servico);
        verify(mapper).toResponse(servico);
    }

    @Test
    void deveFalharAoAtualizarServicoInexistente() {
        when(repository.findById(servicoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.atualizar(servicoId, new UpdateServicoRequestDTO()));
    }

    // =========================
    // deletar
    // =========================
    @Test
    void deveDeletarServicoComSucesso() {
        when(repository.findById(servicoId)).thenReturn(Optional.of(servico));

        service.deletar(servicoId);

        verify(repository).delete(servico);
    }
}
