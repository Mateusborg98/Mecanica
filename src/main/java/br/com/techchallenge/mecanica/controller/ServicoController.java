package br.com.techchallenge.mecanica.controller;

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

import br.com.techchallenge.mecanica.dto.servicoDto.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.servicoDto.UpdateServicoRequestDTO;
import br.com.techchallenge.mecanica.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Serviço", description = "Controller referente a entidade Serviço.")
@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService service;

    @Operation(summary = "Criar serviço")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoResponseDto criar(@RequestBody CreateServicoRequestDto request) {
        return service.criar(request);
    }

    @Operation(summary = "Buscar serviço por ID (UUID)")
    @GetMapping("/{id}")
    public ServicoResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Listar serviços cadastrados (catálogo)")
    @GetMapping
    public List<ServicoResponseDto> listar() {
        return service.listar();
    }

    @Operation(summary = "Atualizar serviço através do ID (UUID)")
    @PutMapping("/{id}")
    public ServicoResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdateServicoRequestDTO request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar serviço através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }

}
