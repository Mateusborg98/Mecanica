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

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.presentation.dto.peca.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.PecaResponseDto;
import br.com.techchallenge.mecanica.presentation.dto.peca.UpdatePecaRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Peça", description = "Controller referente a entidade Peça.")
@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final PecaGateway service;

    @Operation(summary = "Criar peça")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PecaResponseDto criar(@RequestBody CreatePecaRequestDto request) {
        return service.criar(request);
    }

    @Operation(summary = "Buscar peça por ID (UUID)")
    @GetMapping("/{id}")
    public PecaResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Listar peças cadastradas (catálogo)")
    @GetMapping
    public List<PecaResponseDto> listar() {
        return service.listar();
    }

    @Operation(summary = "Atualizar peça através do ID (UUID)")
    @PutMapping("/{id}")
    public PecaResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdatePecaRequestDto request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar peça através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}