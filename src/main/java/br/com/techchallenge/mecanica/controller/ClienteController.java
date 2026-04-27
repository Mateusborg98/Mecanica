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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto criar(@RequestBody CreateClienteRequestDto request) {
        return service.criar(request);
    }

    @GetMapping("/{id}")
    public ClienteResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<ClienteResponseDto> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ClienteResponseDto atualizar(
            @PathVariable UUID id,
            @RequestBody UpdateClienteRequestDto request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}