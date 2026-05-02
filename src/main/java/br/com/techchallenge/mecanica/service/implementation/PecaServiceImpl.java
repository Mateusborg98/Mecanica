package br.com.techchallenge.mecanica.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.estoqueDto.CreateEstoqueRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.CreatePecaRequestDto;
import br.com.techchallenge.mecanica.dto.pecaDto.PecaResponseDto;
import br.com.techchallenge.mecanica.dto.pecaDto.UpdatePecaRequestDto;
import br.com.techchallenge.mecanica.entity.Estoque;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.PecaMapper;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.service.PecaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PecaServiceImpl implements PecaService {

    private final PecaRepository repository;
    private final EstoqueRepository estoqueRepository;
    private final PecaMapper mapper;

    @Override
    public PecaResponseDto criar(CreatePecaRequestDto request) {
        Peca peca = mapper.toEntity(request);
        Peca pecaSalva = repository.save(peca);
        return mapper.toResponseDto(pecaSalva);
    }

    @Override
    public void registrarEntradaEstoque(UUID pecaId, int quantidade) {
        Peca peca = repository.findById(pecaId)
                .orElseThrow(() -> new IllegalArgumentException("Peça não encontrada"));

        Estoque estoque = estoqueRepository.findByPeca(peca)
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));

        estoque.registrarEntrada(quantidade);
        estoqueRepository.save(estoque);
    }

    @Override
    public void registrarSaidaEstoque(UUID pecaId, int quantidade) {
        Peca peca = repository.findById(pecaId)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        Estoque estoque = estoqueRepository.findByPeca(peca)
                .orElseThrow(() -> new RegraNegocioException("Estoque não encontrado"));

        estoque.registrarSaida(quantidade);
        estoqueRepository.save(estoque);
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

    public void criarEstoque(CreateEstoqueRequestDto request) {

        if (request.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade inicial inválida");
        }

        Peca peca = repository.findById(request.getPecaId())
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        boolean jaPossuiEstoque = estoqueRepository.findByPeca(peca).isPresent();

        if (jaPossuiEstoque) {
            throw new RegraNegocioException(
                    "Estoque já cadastrado para esta peça");
        }

        Estoque estoque = new Estoque();
        estoque.setPeca(peca);
        estoque.setQuantidade(request.getQuantidade());

        estoqueRepository.save(estoque);
    }
}
