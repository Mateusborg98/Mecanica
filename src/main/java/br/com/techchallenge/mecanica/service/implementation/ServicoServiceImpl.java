package br.com.techchallenge.mecanica.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.servicoDto.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.dto.servicoDto.ServicoResponseDto;
import br.com.techchallenge.mecanica.dto.servicoDto.UpdateServicoRequestDTO;
import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.mapper.ServicoMapper;
import br.com.techchallenge.mecanica.repository.ServicoRepository;
import br.com.techchallenge.mecanica.service.ServicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicoServiceImpl implements ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper mapper = new ServicoMapper();

    @Override
    public ServicoResponseDto criar(CreateServicoRequestDto request) {
        Servico servico = mapper.toEntity(request);
        return mapper.toResponse(repository.save(servico));
    }

    @Override
    @Transactional(readOnly = true)
    public ServicoResponseDto buscarPorId(UUID id) {
        return mapper.toResponse(buscar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicoResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public ServicoResponseDto atualizar(UUID id, UpdateServicoRequestDTO request) {
        Servico servico = buscar(id);
        mapper.updateEntity(request, servico);
        return mapper.toResponse(servico);
    }

    @Override
    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private Servico buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));
    }
}
