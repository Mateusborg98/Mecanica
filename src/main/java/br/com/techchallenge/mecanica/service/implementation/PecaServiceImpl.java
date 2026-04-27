package br.com.techchallenge.mecanica.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.dto.pecaDto.UpdatePecaRequestDto;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.mapper.PecaMapper;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.service.PecaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class PecaServiceImpl implements PecaService {

    private final PecaRepository repository;
    private final PecaMapper mapper;

    @Override
    public PecaResponseDto criar(CreatePecaRequestDto request) {
        Peca peca = mapper.toEntity(request);
        return mapper.toResponseDto(repository.save(peca));
    }

    @Transactional(readOnly = true)
    public PecaResponseDto buscarPorId(UUID id) {
        Peca peca = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça não encontrada"));
        return mapper.toResponseDto(peca);
    }

    public PecaResponseDto atualizar(UUID id, UpdatePecaRequestDto request) {
        Peca peca = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça não encontrada"));

        mapper.updateEntity(request, peca);
        return mapper.toResponseDto(peca);
    }

    @Override
    public List<PecaResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private Peca buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça não encontrada"));

    }

}
