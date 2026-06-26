package br.com.techchallenge.mecanica.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.presentation.dto.ordemDeServico.AddServicoPecaOrdemDeServicoDto;
import br.com.techchallenge.mecanica.presentation.dto.ordemDeServico.CriarOrdemDeServicoRequest;
import br.com.techchallenge.mecanica.presentation.dto.ordemDeServico.OrdemDeServicoResponse;
import br.com.techchallenge.mecanica.presentation.dto.ordemDeServico.TempoMedioServicoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Ordem de Serviço", description = "Controller referente a entidade Ordem de Serviço.")
@RestController
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
public class OrdemDeServicoController {

    private final OrdemDeServicoGateway service;

    @Operation(summary = "Criar ordem de serviço")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemDeServicoResponse criar(@RequestBody CriarOrdemDeServicoRequest request) {
        return service.criar(request);
    }

    @Operation(summary = "Buscar ordem de serviço por ID (UUID)")
    @GetMapping("/{id}")
    public OrdemDeServicoResponse buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Listar ondens de serviço")
    @GetMapping
    public List<OrdemDeServicoResponse> listar() {
        return service.listar();
    }

    @Operation(summary = "Colocar ordem de serviço em diagnostico")
    @PostMapping("/{id}/iniciar-diagnostico")
    public OrdemDeServicoResponse iniciarDiagnostico(@PathVariable UUID id) {
        return service.iniciarDiagnostico(id);
    }

    @Operation(summary = "Cliente aprovar orçamento através do ID (UUID)")
    @PostMapping("/{id}/aprovar-orcamento")
    public OrdemDeServicoResponse aprovarOrcamento(@PathVariable UUID id) {
        return service.aprovarOrcamento(id);
    }

    @Operation(summary = "Cliente negar orçamento através do ID (UUID)")
    @PostMapping("/{id}/negar-orcamento")
    public OrdemDeServicoResponse negarOrcamento(@PathVariable UUID id) {
        return service.negarOrcamento(id);
    }

    @Operation(summary = "Finalizar ordem de serviço")
    @PostMapping("/{id}/finalizar")
    public OrdemDeServicoResponse finalizar(@PathVariable UUID id) {
        return service.finalizar(id);
    }

    @Operation(summary = "Entregar ordem de serviço")
    @PostMapping("/{id}/entregar")
    public OrdemDeServicoResponse entregar(@PathVariable UUID id) {
        return service.entregar(id);
    }

    @Operation(summary = "Consultar tempo médio dos serviços")
    @GetMapping("/tempo-medio-servicos")
    public ResponseEntity<List<TempoMedioServicoResponseDto>> calcularTempoMedioServicos() {

        return ResponseEntity.ok(
                service.calcularTempoMedioServicos());
    }

    @Operation(summary = "Adicionar serviços e peças/insumos na ordem de serviço")
    @PostMapping("/{id}/adicionar-servicos-pecas")
    public OrdemDeServicoResponse adicionarServicoPeca(
            @PathVariable UUID id,
            @RequestBody @Valid AddServicoPecaOrdemDeServicoDto request) {
        return service.adicionarServicoPeca(id, request);
    }
}
