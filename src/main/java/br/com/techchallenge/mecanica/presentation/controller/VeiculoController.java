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

import br.com.techchallenge.mecanica.application.usecase.veiculo.AtualizarIdClienteDoVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.AtualizarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.BuscarVeiculoPorIdUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.BuscarVeiculoPorPlacaUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.CriarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.DeletarVeiculoUseCase;
import br.com.techchallenge.mecanica.application.usecase.veiculo.ListarVeiculosUseCase;
import br.com.techchallenge.mecanica.domain.veiculo.Veiculo;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.VeiculoMapper;
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
    private final AtualizarIdClienteDoVeiculoUseCase atualizarIdClienteDoVeiculoUseCase;
    private final CriarVeiculoUseCase criarVeiculoUseCase;
    private final BuscarVeiculoPorIdUseCase buscarVeiculoPorIdUseCase;
    private final BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase;
    private final ListarVeiculosUseCase listarVeiculosUseCase;
    private final DeletarVeiculoUseCase deletarVeiculoUseCase;

    private final VeiculoMapper veiculoMapper;

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veiculo")
    public ResponseEntity<VeiculoResponse> atualizar(@PathVariable UUID id,
            @RequestBody AtualizarVeiculoRequest request) {

        Veiculo veiculo = atualizarVeiculoUseCase.executar(id, request);

        VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);

        return ResponseEntity.ok(veiculoResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar o ID do veiculo vinculado ao veiculo")
    public ResponseEntity<VeiculoResponse> atualizarIdVeiculoDoVeiculo(@PathVariable UUID id,
            @RequestBody AtualizarVeiculoRequest request) {

        Veiculo veiculo = atualizarIdClienteDoVeiculoUseCase.executar(id, request.cliente().getId());

        VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);

        return ResponseEntity.ok(veiculoResponse);
    }

    @PostMapping
    @Operation(summary = "Criar veículo")
    public ResponseEntity<VeiculoResponse> criar(@RequestBody CriarVeiculoRequest request) {
        Veiculo veiculo = criarVeiculoUseCase.executar(request);

        VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);

        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID (UUID)", hidden = true)
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable UUID id) {
        Veiculo veiculo = buscarVeiculoPorIdUseCase.executar(id);

        VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);

        return ResponseEntity.ok(veiculoResponse);
    }

    @GetMapping("/placa/{placa}")
    @Operation(summary = "Buscar veículo por placa")
    public ResponseEntity<VeiculoResponse> buscarPorPlaca(@PathVariable String placa) {
        Veiculo veiculo = buscarVeiculoPorPlacaUseCase.executar(placa);

        VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);

        return ResponseEntity.ok(veiculoResponse);
    }

    @GetMapping
    @Operation(summary = "Listar veículos cadastrados")
    public ResponseEntity<List<VeiculoResponse>> listar() {
        List<Veiculo> veiculos = listarVeiculosUseCase.executar();

        List<VeiculoResponse> veiculoResponses = veiculos.stream().map(veiculoMapper::toResponse).toList();

        return ResponseEntity.ok(veiculoResponses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veículo através do ID (UUID)")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarVeiculoUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

}
