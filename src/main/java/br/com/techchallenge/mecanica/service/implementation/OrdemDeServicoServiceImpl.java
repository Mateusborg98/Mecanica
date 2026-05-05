package br.com.techchallenge.mecanica.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Estoque;
import br.com.techchallenge.mecanica.entity.ItemOrdemDeServico;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.OrdemServicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdemDeServicoServiceImpl implements OrdemServicoService {

    private final PecaServiceImpl pecaServiceImpl;
    private final OrdemDeServicoRepository ordemRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final PecaRepository pecaRepository;
    private final EstoqueRepository estoqueRepository;
    private final OrdemDeServicoMapper mapper;

    @Override
    public OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado"));

        OrdemDeServico os = new OrdemDeServico();
        os.setCliente(cliente);
        os.setVeiculo(veiculo);
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);
        os.setDtInicioOs(LocalDateTime.now());

        ordemRepository.save(os);
        return mapper.toResponse(os);
    }

    public OrdemDeServicoResponseDto adicionarPecaNaOs(
            UUID ordemId,
            UUID pecaId,
            int quantidade) {
        OrdemDeServico os = ordemRepository.findById(ordemId)
                .orElseThrow(() -> new RegraNegocioException("OS não encontrada"));

        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

        pecaServiceImpl.registrarSaidaEstoque(pecaId, quantidade);
        os.adicionarPeca(peca, quantidade);

        ordemRepository.save(os);
        return mapper.toResponse(os);
    }

    @Override
    public OrdemDeServicoResponseDto iniciarDiagnostico(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.RECEBIDA);
        os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    public OrdemDeServicoResponseDto enviarParaAprovacao(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    public OrdemDeServicoResponseDto finalizar(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.EM_EXECUCAO);
        os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);
        os.setDtFimOs(LocalDateTime.now());
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    public OrdemDeServicoResponseDto entregar(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.FINALIZADA);
        os.setStatus(StatusOrdemDeServicoEnum.ENTREGUE);
        return mapper.toResponse(ordemRepository.save(os));
    }

    private OrdemDeServico buscar(UUID id) {
        return ordemRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("OS não encontrada"));
    }

    private void validarStatus(OrdemDeServico os, StatusOrdemDeServicoEnum esperado) {
        if (os.getStatus() != esperado) {
            throw new RegraNegocioException(
                    "Transição inválida de status. Status atual: " + os.getStatus());
        }
    }

    @Override
    public OrdemDeServicoResponseDto aprovarOrcamento(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        os.setStatus(StatusOrdemDeServicoEnum.ORCAMENTO_APROVADO);
        return mapper.toResponse(os);
    }

    @Override
    public OrdemDeServicoResponseDto iniciarExecucao(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.ORCAMENTO_APROVADO);
        baixarEstoque(os);
        os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);
        os.setDtInicioOs(LocalDateTime.now());
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    @Transactional(readOnly = true)
    public OrdemDeServicoResponseDto buscarPorId(UUID id) {
        return mapper.toResponse(buscarOrdem(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdemDeServicoResponseDto> listar() {
        return ordemRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private OrdemDeServico buscarOrdem(UUID id) {
        return ordemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada"));
    }

    private void baixarEstoque(OrdemDeServico os) {
        for (ItemOrdemDeServico item : os.getItens()) {

            Estoque estoque = estoqueRepository.findByPeca(item.getPeca())
                    .orElseThrow(() -> new RegraNegocioException(
                            "Estoque não encontrado para a peça: " + item.getPeca().getNome()));

            if (estoque.getQuantidade() < item.getQuantidade()) {
                throw new RegraNegocioException(
                        "Estoque insuficiente para a peça: " + item.getPeca().getNome());
            }

            estoque.setQuantidade(
                    estoque.getQuantidade() - item.getQuantidade());
        }
    }

}
