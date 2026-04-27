package br.com.techchallenge.mecanica.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.veiculoDto.CreateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.UpdateVeiculoRequestDto;
import br.com.techchallenge.mecanica.dto.veiculoDto.VeiculoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.VeiculoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.VeiculoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository repository;
    private final VeiculoMapper mapper = new VeiculoMapper();
    private final ClienteRepository clienteRepository;

    public VeiculoResponseDto criar(CreateVeiculoRequestDto request) {

        if (repository.existsByPlaca(request.getPlaca())) {
            throw new RegraNegocioException("Placa já cadastrada");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Veiculo veiculo = mapper.toEntity(request, cliente);
        return mapper.toResponse(repository.save(veiculo));
    }

    @Transactional(readOnly = true)
    public VeiculoResponseDto buscarPorId(UUID id) {
        return mapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public VeiculoResponseDto atualizar(UUID id, UpdateVeiculoRequestDto request) {
        Veiculo veiculo = buscar(id);
        mapper.updateEntity(request, veiculo);
        return mapper.toResponse(veiculo);
    }

    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private Veiculo buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }

    @Override
    public VeiculoResponseDto buscarPorPlaca(String placa) {
        Veiculo veiculo = repository.findByPlaca(placa);
        return mapper.toResponse(veiculo);
    }

}