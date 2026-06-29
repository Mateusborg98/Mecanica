package br.com.techchallenge.mecanica.presentation.controller;

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

import br.com.techchallenge.mecanica.application.usecase.servico.AtualizarServicoUseCase;
import br.com.techchallenge.mecanica.application.usecase.servico.BuscarServicoPorIdUseCase;
import br.com.techchallenge.mecanica.application.usecase.servico.CriarServicoUseCase;
import br.com.techchallenge.mecanica.application.usecase.servico.InativarServicoUseCase;
import br.com.techchallenge.mecanica.application.usecase.servico.ListarServicosUseCase;
import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.presentation.dto.servico.AtualizarServicoRequest;
import br.com.techchallenge.mecanica.presentation.dto.servico.CriarServicoRequest;
import br.com.techchallenge.mecanica.presentation.dto.servico.ServicoResponse;
import br.com.techchallenge.mecanica.presentation.mapper.ServicoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Serviço", description = "Controller referente a entidade Serviço.")
@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final CriarServicoUseCase criarServicoUseCase;
    private final BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;
    private final ListarServicosUseCase listarServicosUseCase;
    private final AtualizarServicoUseCase atualizarServicoUseCase;
    private final InativarServicoUseCase inativarServicoUseCase;
    private final ServicoPresentationMapper mapper;

    @Operation(summary = "Criar serviço")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoResponse criar(@RequestBody CriarServicoRequest request) {
        Servico servico = criarServicoUseCase.executar(
                mapper.toInput(request));

        return mapper.toResponse(servico);
    }

    @Operation(summary = "Buscar serviço por ID (UUID)")
    @GetMapping("/{id}")
    public ServicoResponse buscarPorId(@PathVariable UUID id) {
        Servico servico = buscarServicoPorIdUseCase.executar(id);

        return mapper.toResponse(servico);
    }

    @Operation(summary = "Listar serviços cadastrados (catálogo)")
    @GetMapping
    public List<ServicoResponse> listar() {
        return listarServicosUseCase.executar()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Operation(summary = "Atualizar serviço através do ID (UUID)")
    @PutMapping("/{id}")
    public ServicoResponse atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarServicoRequest request) {
        Servico servico = atualizarServicoUseCase.executar(
                id,
                mapper.toInput(request));

        return mapper.toResponse(servico);
    }

    @Operation(summary = "Deletar serviço através do ID (UUID)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        inativarServicoUseCase.executar(id);
    }

}
