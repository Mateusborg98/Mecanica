package br.com.techchallenge.mecanica.presentation.cliente;

import br.com.techchallenge.mecanica.application.usecase.cliente.*;
import br.com.techchallenge.mecanica.domain.cliente.Cliente;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Cliente", description = "Controller referente a entidade Cliente.")
@RequiredArgsConstructor
public class ClienteController {

    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final CriarClienteUseCase criarClienteUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final BuscarClientePorCpfCnpjUseCase buscarClientePorCpfCnpjUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;
    private final ClienteMapper clienteMapper;

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id, @RequestBody AtualizarClienteRequest request) {

        Cliente cliente = atualizarClienteUseCase.executar(id, request);

        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        return ResponseEntity.ok(clienteResponse);
    }

    @PostMapping
    @Operation(summary = "Criar cliente")
    public ResponseEntity<ClienteResponse> criar(@RequestBody CriarClienteRequest request) {

        Cliente cliente = criarClienteUseCase.executar(request);

        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por id (UUID)", hidden = true)
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable UUID id) {

        Cliente cliente = buscarClientePorIdUseCase.executar(id);

        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        return ResponseEntity.ok(clienteResponse);
    }

    @GetMapping("/cpf-cnpj/{cpfCnpj}")
    @Operation(summary = "Buscar cliente por CPF/CNPJ")
    public ResponseEntity<ClienteResponse> buscarPorCpf(@PathVariable String cpfCnpj) {

        Cliente cliente = buscarClientePorCpfCnpjUseCase.executar(cpfCnpj);

        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        return ResponseEntity.ok(clienteResponse);
    }

    @GetMapping
    @Operation(summary = "Listar clientes cadastrados")
    public ResponseEntity<List<ClienteResponse>> listar() {

        List<Cliente> clientes = listarClientesUseCase.executar();

        List<ClienteResponse> clienteResponses = clientes.stream()
                .map(clienteMapper::toResponse)
                .toList();

        return ResponseEntity.ok(clienteResponses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente através do ID (UUID)")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        deletarClienteUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}