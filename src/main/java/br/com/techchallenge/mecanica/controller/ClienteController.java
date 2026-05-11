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

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResponseDto;
import br.com.techchallenge.mecanica.dto.clienteDto.CreateClienteRequestDto;
import br.com.techchallenge.mecanica.dto.clienteDto.UpdateClienteRequestDto;
import br.com.techchallenge.mecanica.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cliente", description = "Controller referente a entidade Cliente.")
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Criar cliente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto criar(@RequestBody CreateClienteRequestDto request) {
        return service.criar(request);
    }

    @Operation(summary = "Buscar cliente por id (UUID)", hidden = true)
    @GetMapping("/id/{id}")
    public ClienteResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Buscar cliente por CPF/CNPJ")
    @GetMapping("/cpf-cnpj/{cpfCnpj}")
    public ClienteResponseDto buscarPorCpf(@PathVariable String cpfCnpj) {
        return service.buscarPorCpfCnpj(cpfCnpj);
    }

    @Operation(summary = "Listar clientes cadastrados")
    @GetMapping
    public List<ClienteResponseDto> listar() {
        return service.listar();
    }

    @Operation(summary = "Atualizar cliente através do ID (UUID)")
    @PutMapping("/{id}")
    public ClienteResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdateClienteRequestDto request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar cliente através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}