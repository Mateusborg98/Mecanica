package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.gateway.PecaGateway;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.PecaMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.EstoqueJpaRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.PecaRepository;
import br.com.techchallenge.mecanica.presentation.peca.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.presentation.peca.PecaResponseDto;
import br.com.techchallenge.mecanica.presentation.peca.UpdatePecaRequestDto;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PecaGatewayImpl implements PecaGateway {

    private final PecaRepository repository;
    private final EstoqueJpaRepository estoqueJpaRepository;
    private final PecaMapper mapper;

    @Override
    @Transactional
    public PecaResponseDto criar(CreatePecaRequestDto request) {

        PecaJpaEntity pecaJpaEntity = mapper.toEntity(request);
        PecaJpaEntity pecaJpaEntityCriada = repository.save(pecaJpaEntity);

        criarEstoque(pecaJpaEntityCriada, request.getQuantidadeInicial());

        return mapper.toResponseDto(pecaJpaEntityCriada);
    }

    public void criarEstoque(PecaJpaEntity pecaJpaEntity, int quantidade) {

        if (quantidade < 0) {
            throw new RegraNegocioException("Quantidade inicial inválida");
        }

        boolean jaPossuiEstoque = estoqueJpaRepository.existsByPecaId(pecaJpaEntity.getId());

        if (jaPossuiEstoque) {
            throw new RegraNegocioException("Estoque já cadastrado para esta peça");
        }

        EstoqueJpaEntity estoqueJpaEntity = new EstoqueJpaEntity();
        estoqueJpaEntity.setPecaJpaEntity(pecaJpaEntity);
        estoqueJpaEntity.setQuantidade(quantidade);

        estoqueJpaRepository.save(estoqueJpaEntity);
    }

    @Override
    @Transactional
    public void registrarEntradaEstoque(UUID pecaId, int quantidade) {
        PecaJpaEntity pecaJpaEntity = repository.findById(pecaId)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        EstoqueJpaEntity estoqueJpaEntity = estoqueJpaRepository.findByPeca(pecaJpaEntity)
                .orElseThrow(() -> new RegraNegocioException("Estoque não encontrado"));

        estoqueJpaEntity.registrarEntrada(quantidade);
        estoqueJpaRepository.save(estoqueJpaEntity);
    }

    @Override
    @Transactional
    public void registrarSaidaEstoque(UUID pecaId, int quantidade) {
        PecaJpaEntity pecaJpaEntity = repository.findById(pecaId)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        EstoqueJpaEntity estoqueJpaEntity = estoqueJpaRepository.findByPeca(pecaJpaEntity)
                .orElseThrow(() -> new RegraNegocioException("Estoque não encontrado"));

        estoqueJpaEntity.registrarSaida(quantidade);
        estoqueJpaRepository.save(estoqueJpaEntity);
    }

    @Override
    public PecaResponseDto buscarPorId(UUID id) {
        PecaJpaEntity pecaJpaEntity = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));
        return mapper.toResponseDto(pecaJpaEntity);
    }

    @Override
    @Transactional
    public PecaResponseDto atualizar(UUID id, UpdatePecaRequestDto request) {
        PecaJpaEntity pecaJpaEntity = repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        mapper.updateEntity(request, pecaJpaEntity);
        return mapper.toResponseDto(pecaJpaEntity);
    }

    @Override
    public List<PecaResponseDto> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        repository.delete(buscar(id));
    }

    private PecaJpaEntity buscar(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

    }

}
