package br.com.techchallenge.mecanica.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.clienteDto.ClienteResponseDto;
import br.com.techchallenge.mecanica.dto.clienteDto.CreateClienteRequestDto;
import br.com.techchallenge.mecanica.dto.clienteDto.UpdateClienteRequestDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.ClienteMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper = new ClienteMapper();

    public ClienteResponseDto criar(CreateClienteRequestDto request) {
        if (repository.existsByCpfCnpj(request.getCpfCnpj())) {
            throw new RegraNegocioException("CPF/CNPJ já cadastrado");
        }

        Cliente cliente = mapper.toEntity(request);
        return mapper.toResponse(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDto buscarPorId(UUID id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ClienteResponseDto atualizar(UUID id, UpdateClienteRequestDto request) {
        Cliente cliente = buscar(id);
        mapper.updateEntity(request, cliente);
        return mapper.toResponse(cliente);
    }

    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private Cliente buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public boolean buscarPorCpfCnpj(CreateClienteRequestDto request) {
        return repository.existsByCpfCnpj(request.getCpfCnpj());
    }

    public ClienteResponseDto buscarPorCpfCnpj(String cpfCnpj) {
        Cliente cliente = repository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return mapper.toResponse(cliente);
    }

}