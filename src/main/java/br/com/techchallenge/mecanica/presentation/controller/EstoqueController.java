package br.com.techchallenge.mecanica.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.application.dto.estoque.MovimentarEstoqueInput;
import br.com.techchallenge.mecanica.application.usecase.estoque.BuscarEstoquePorPecaUseCase;
import br.com.techchallenge.mecanica.application.usecase.estoque.ListarEstoquesUseCase;
import br.com.techchallenge.mecanica.application.usecase.estoque.RegistrarEntradaEstoqueUseCase;
import br.com.techchallenge.mecanica.application.usecase.estoque.RegistrarSaidaEstoqueUseCase;
import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.presentation.dto.estoque.CreateEstoqueRequestDto;
import br.com.techchallenge.mecanica.presentation.dto.estoque.EstoqueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Estoque", description = "Consulta e movimentação do estoque de peças da oficina.")
@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final RegistrarEntradaEstoqueUseCase registrarEntrada;
    private final RegistrarSaidaEstoqueUseCase registrarSaida;
    private final BuscarEstoquePorPecaUseCase buscarPorPeca;
    private final ListarEstoquesUseCase listarEstoques;

    @Operation(
            summary = "Registrar entrada no estoque",
            description = "Adiciona a quantidade informada ao saldo da peça, criando o estoque quando necessário.")
    @PostMapping("/entradas")
    public ResponseEntity<EstoqueResponse> registrarEntrada(
            @RequestBody @Valid CreateEstoqueRequestDto request) {
        return ResponseEntity.ok(toResponse(registrarEntrada.executar(toInput(request))));
    }

    @Operation(
            summary = "Registrar saída do estoque",
            description = "Retira a quantidade informada do saldo da peça após validar a disponibilidade.")
    @PostMapping("/saidas")
    public ResponseEntity<EstoqueResponse> registrarSaida(
            @RequestBody @Valid CreateEstoqueRequestDto request) {
        return ResponseEntity.ok(toResponse(registrarSaida.executar(toInput(request))));
    }

    @Operation(
            summary = "Consultar estoque por peça",
            description = "Retorna o saldo de estoque associado ao identificador da peça.")
    @GetMapping("/pecas/{pecaId}")
    public EstoqueResponse buscarPorPeca(@PathVariable UUID pecaId) {
        return toResponse(buscarPorPeca.executar(pecaId));
    }

    @Operation(
            summary = "Listar estoques",
            description = "Retorna os saldos de estoque cadastrados para todas as peças.")
    @GetMapping
    public List<EstoqueResponse> listar() {
        return listarEstoques.executar().stream().map(this::toResponse).toList();
    }

    private MovimentarEstoqueInput toInput(CreateEstoqueRequestDto request) {
        return new MovimentarEstoqueInput(request.getPecaId(), request.getQuantidade());
    }

    private EstoqueResponse toResponse(Estoque estoque) {
        return new EstoqueResponse(estoque.getId(), estoque.getPecaId(), estoque.getQuantidade());
    }
}
