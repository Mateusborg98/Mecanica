package br.com.techchallenge.mecanica.controller;

import java.time.Duration;
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

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.TempoMedioExecucaoResponseDto;
import br.com.techchallenge.mecanica.service.OrdemServicoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
public class OrdemDeServicoController {

    private final OrdemServicoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemDeServicoResponseDto criar(
            @RequestBody CreateOrdemDeServicoRequestDto request) {
        return service.criar(request);
    }

    @GetMapping("/{id}")
    public OrdemDeServicoResponseDto buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<OrdemDeServicoResponseDto> listar() {
        return service.listar();
    }

    @PostMapping("/{id}/enviar-aprovacao")
    public OrdemDeServicoResponseDto enviarParaAprovacao(@PathVariable UUID id) {
        return service.enviarParaAprovacao(id);
    }

    @PostMapping("/{id}/iniciar-diagnostico")
    public OrdemDeServicoResponseDto iniciarDiagnostico(@PathVariable UUID id) {
        return service.iniciarDiagnostico(id);
    }

    @PostMapping("/{id}/aprovar-orcamento")
    public OrdemDeServicoResponseDto aprovarOrcamento(@PathVariable UUID id) {
        return service.aprovarOrcamento(id);
    }

    @PostMapping("/{id}/iniciar-execucao")
    public OrdemDeServicoResponseDto iniciarExecucao(@PathVariable UUID id) {
        return service.iniciarExecucao(id);
    }

    @PostMapping("/{id}/finalizar")
    public OrdemDeServicoResponseDto finalizar(@PathVariable UUID id) {
        return service.finalizar(id);
    }

    @PostMapping("/{id}/entregar")
    public OrdemDeServicoResponseDto entregar(@PathVariable UUID id) {
        return service.entregar(id);
    }

    @GetMapping("/ordens/tempo-medio-execucao")
    public ResponseEntity<TempoMedioExecucaoResponseDto> obterTempoMedioExecucao() {

        Duration tempoMedio = service.calcularTempoMedioExecucao();

        return ResponseEntity.ok(
                new TempoMedioExecucaoResponseDto(tempoMedio));
    }
}
