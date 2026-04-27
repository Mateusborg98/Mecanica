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

import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;
import br.com.techchallenge.mecanica.service.VeiculoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoResponseDto criar(@RequestBody CreateVeiculoRequestDto request) {
        return service.criar(request);
    }

    @GetMapping("/{id}")
    public VeiculoResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<VeiculoResponseDto> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public VeiculoResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdateVeiculoRequestDto request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }

}
