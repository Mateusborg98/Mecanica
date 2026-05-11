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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Veículo", description = "Controller referente a entidade Veículo.")
@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @Operation(summary = "Criar veículo")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoResponseDto criar(@RequestBody CreateVeiculoRequestDto request) {
        return service.criar(request);
    }

    @Operation(summary = "Buscar veículo por ID (UUID)", hidden = true)
    @GetMapping("/{id}")
    public VeiculoResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Buscar veículo por placa")
    @GetMapping("/placa/{placa}")
    public VeiculoResponseDto buscarPorPlaca(@PathVariable String placa) {
        return service.buscarPorPlaca(placa);
    }

    @Operation(summary = "Listar veículos cadastrados")
    @GetMapping
    public List<VeiculoResponseDto> listar() {
        return service.listar();
    }

    @Operation(summary = "Atualizar veículo através do ID (UUID)")
    @PutMapping("/{id}")
    public VeiculoResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdateVeiculoRequestDto request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar veículo através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }

}
