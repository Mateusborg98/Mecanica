package br.com.techchallenge.mecanica.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.application.usecase.peca.AtualizarPecaUseCase;
import br.com.techchallenge.mecanica.application.usecase.peca.BuscarPecaPorIdUseCase;
import br.com.techchallenge.mecanica.application.usecase.peca.CriarPecaUseCase;
import br.com.techchallenge.mecanica.application.usecase.peca.InativarPecaUseCase;
import br.com.techchallenge.mecanica.application.usecase.peca.ListarPecasUseCase;
import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.presentation.dto.peca.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.PecaResponseDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.UpdatePecaRequestDto;
import br.com.techchallenge.mecanica.presentation.mapper.PecaPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Peça", description = "Controller referente a entidade Peça.")
@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final CriarPecaUseCase criarPecaUseCase;
    private final BuscarPecaPorIdUseCase buscarPecaPorIdUseCase;
    private final ListarPecasUseCase listarPecasUseCase;
    private final AtualizarPecaUseCase atualizarPecaUseCase;
    private final InativarPecaUseCase inativarPecaUseCase;
    private final PecaPresentationMapper mapper;

    @Operation(summary = "Criar peça")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PecaResponseDto criar(@RequestBody CreatePecaRequestDto request) {
        Peca peca = criarPecaUseCase.executar(
                mapper.toInput(request));

        return mapper.toResponse(peca);
    }

    @Operation(summary = "Buscar peça por ID (UUID)")
    @GetMapping("/{id}")
    public PecaResponseDto buscarPorId(@PathVariable UUID id) {
        Peca peca = buscarPecaPorIdUseCase.executar(id);

        return mapper.toResponse(peca);
    }

    @Operation(summary = "Listar peças cadastradas (catálogo)")
    @GetMapping
    public List<PecaResponseDto> listar() {
        return listarPecasUseCase.executar()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Operation(summary = "Atualizar peça através do ID (UUID)")
    @PutMapping("/{id}")
    public PecaResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdatePecaRequestDto request) {
        Peca peca = atualizarPecaUseCase.executar(
                id,
                mapper.toInput(request));

        return mapper.toResponse(peca);
    }

    @Operation(summary = "Inativar peça através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        inativarPecaUseCase.executar(id);
    }
}