package br.com.techchallenge.mecanica.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.service.PecaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final PecaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PecaResponseDto criar(@RequestBody CreatePecaRequestDto request) {
        return service.criar(request);
    }

    @GetMapping("/{id}")
    public PecaResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<PecaResponseDto> listar() {
        return service.listar();
    }
}