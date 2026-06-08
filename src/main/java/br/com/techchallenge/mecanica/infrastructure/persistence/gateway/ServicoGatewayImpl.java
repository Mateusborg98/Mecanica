package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.gateway.ServicoGateway;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ServicoRepository;
import br.com.techchallenge.mecanica.mapper.ServicoMapper;
import br.com.techchallenge.mecanica.presentation.servico.CreateServicoRequestDto;
import br.com.techchallenge.mecanica.presentation.servico.ServicoResponseDto;
import br.com.techchallenge.mecanica.presentation.servico.UpdateServicoRequestDTO;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicoGatewayImpl implements ServicoGateway {

    private final ServicoRepository repository;
    private final ServicoMapper mapper;

    @Override
    @Transactional
    public ServicoResponseDto criar(CreateServicoRequestDto request) {
        ServicoJpaEntity servicoJpaEntity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(servicoJpaEntity));
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
    @Transactional
    public ServicoResponseDto atualizar(UUID id, UpdateServicoRequestDTO request) {
        ServicoJpaEntity servicoJpaEntity = buscar(id);
        mapper.updateEntity(request, servicoJpaEntity);
        return mapper.toResponse(repository.save(servicoJpaEntity));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private ServicoJpaEntity buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado"));
    }

}