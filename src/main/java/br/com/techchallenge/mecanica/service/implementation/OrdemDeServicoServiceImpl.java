package br.com.techchallenge.mecanica.service.implementation;

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
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.EstoqueRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.ServicoRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.service.OrdemServicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdemDeServicoServiceImpl implements OrdemServicoService {

    private final OrdemDeServicoRepository ordemRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final PecaRepository pecaRepository;
    private final EstoqueRepository estoqueRepository;
    private final OrdemDeServicoMapper mapper;

    @Override
    public OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        OrdemDeServico os = mapper.toEntity(request, veiculo, cliente);
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);

        request.getServicos().forEach(s -> os.adicionarServico(
                servicoRepository.findById(s.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"))));

        request.getItens().forEach(i -> os.adicionarPeca(
                pecaRepository.findById(i.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Peça não encontrada")),
                i.getQuantidade()));

        os.calcularValorTotal();
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    public OrdemDeServicoResponseDto enviarParaAprovacao(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.RECEBIDA);
        os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        return mapper.toResponse(os);
    }

    @Override
    public OrdemDeServicoResponseDto iniciarDiagnostico(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        return mapper.toResponse(os);
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
        return mapper.toResponse(os);
    }

    @Override
    public OrdemDeServicoResponseDto finalizar(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.EM_EXECUCAO);
        os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);
        return mapper.toResponse(os);
    }

    @Override
    public OrdemDeServicoResponseDto entregar(UUID ordemId) {
        OrdemDeServico os = buscarOrdem(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.FINALIZADA);
        os.setStatus(StatusOrdemDeServicoEnum.ENTREGUE);
        return mapper.toResponse(os);
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

    private void validarStatus(OrdemDeServico os, StatusOrdemDeServicoEnum statusEsperado) {
        if (os.getStatus() != statusEsperado) {
            throw new RegraNegocioException(
                    "Transição inválida de status. Status atual: " + os.getStatus());
        }
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
