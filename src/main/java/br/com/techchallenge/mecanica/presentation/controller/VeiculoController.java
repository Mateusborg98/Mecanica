package br.com.techchallenge.mecanica.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.application.usecase.veiculo.AlterarClienteDoVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.AtualizarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.BuscarVeiculoPorIdUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.BuscarVeiculoPorPlacaUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.CriarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.InativarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.ListarVeiculosUseCase;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.VeiculoMapper;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.AtualizarClienteDoVeiculoRequest;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.AtualizarVeiculoRequest;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.CriarVeiculoRequest;
import br.com.techchallenge.mecanica.presentation.dto.veiculo.VeiculoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/veiculos")
@Tag(name = "Veículo", description = "Controller referente a entidade Veículo.")
@RequiredArgsConstructor
public class VeiculoController {

    private final AtualizarVeiculoUseCase atualizarVeiculoUseCase;
    private final AlterarClienteDoVeiculoUseCase alterarClienteDoVeiculoUseCase;
    private final CriarVeiculoUseCase criarVeiculoUseCase;
    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;
    private final BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase;
    private final ListarVeiculosUseCase listarVeiculosUseCase;
    private final InativarVeiculoUseCase inativarVeiculoUseCase;

    private final VeiculoMapper veiculoMapper;

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo")
    public ResponseEntity<VeiculoResponse> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarVeiculoRequest request) {

        Veiculo veiculo = atualizarVeiculoUseCase.executar(id, request);

        return ResponseEntity.ok(
                veiculoMapper.toResponse(veiculo));
    }

    @PutMapping("/{id}/cliente")
    @Operation(summary = "Alterar cliente do veículo")
    public ResponseEntity<VeiculoResponse> alterarCliente(
            @PathVariable UUID id,
            @RequestBody AtualizarClienteDoVeiculoRequest request) {

        Veiculo veiculo = alterarClienteDoVeiculoUseCase.executar(
                id,
                request.clienteId());

        return ResponseEntity.ok(
                veiculoMapper.toResponse(veiculo));
    }

    @PostMapping
    @Operation(summary = "Criar veículo")
    public ResponseEntity<VeiculoResponse> criar(@RequestBody CriarVeiculoRequest request) {

        Veiculo veiculo = criarVeiculoUseCase.executar(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(veiculoMapper.toResponse(veiculo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID (UUID)")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable UUID id) {

        Veiculo veiculo = buscarVeiculoPorIdUseCase.executar(id);

        return ResponseEntity.ok(
                veiculoMapper.toResponse(veiculo));
    }

    @GetMapping("/placa/{placa}")
    @Operation(summary = "Buscar veículo por placa")
    public ResponseEntity<VeiculoResponse> buscarPorPlaca(@PathVariable String placa) {

        Veiculo veiculo = buscarVeiculoPorPlacaUseCase.executar(placa);

        return ResponseEntity.ok(
                veiculoMapper.toResponse(veiculo));
    }

    @GetMapping
    @Operation(summary = "Listar veículos cadastrados")
    public ResponseEntity<List<VeiculoResponse>> listar() {

        List<Veiculo> veiculos = listarVeiculosUseCase.executar();

        return ResponseEntity.ok(
                veiculos.stream()
                        .map(veiculoMapper::toResponse)
                        .toList());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar veículo")
    public ResponseEntity<Void> inativar(@PathVariable UUID id) {

        inativarVeiculoUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
