package br.com.techchallenge.mecanica.service.implementation;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.AddServicoPecaOrdemDeServicoDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.dto.ordemDeServicoDto.TempoMedioServicoResponseDto;
import br.com.techchallenge.mecanica.entity.Cliente;
import br.com.techchallenge.mecanica.entity.Operador;
import br.com.techchallenge.mecanica.entity.OrdemDeServico;
import br.com.techchallenge.mecanica.entity.Peca;
import br.com.techchallenge.mecanica.entity.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.entity.Servico;
import br.com.techchallenge.mecanica.entity.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.entity.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.entity.StatusServicoEnum;
import br.com.techchallenge.mecanica.entity.Veiculo;
import br.com.techchallenge.mecanica.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.repository.ClienteRepository;
import br.com.techchallenge.mecanica.repository.OperadorRepository;
import br.com.techchallenge.mecanica.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.repository.PecaRepository;
import br.com.techchallenge.mecanica.repository.ServicoRepository;
import br.com.techchallenge.mecanica.repository.VeiculoRepository;
import br.com.techchallenge.mecanica.security.UsuarioAutenticadoService;
import br.com.techchallenge.mecanica.service.OrdemServicoService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdemDeServicoServiceImpl implements OrdemServicoService {

    private final OrdemDeServicoRepository ordemRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final PecaRepository pecaRepository;
    private final ServicoRepository servicoRepository;
    private final OperadorRepository operadorRepository;
    private final PecaServiceImpl pecaService;
    private final UsuarioAutenticadoService autenticadoService;
    private final OrdemDeServicoMapper mapper;

    @Override
    @Transactional
    public OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request) {

        Cliente cliente = clienteRepository.findByCpfCnpj(request.getCpfCnpj())
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado"));

        Veiculo veiculo = veiculoRepository.findByPlaca(request.getPlaca())
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado"));

        Operador operador = operadorRepository
                .findByMatricula(autenticadoService.getMatricula())
                .orElseThrow(() -> new RegraNegocioException("Operador não encontrado"));

        OrdemDeServico os = new OrdemDeServico();
        os.setCliente(cliente);
        os.setVeiculo(veiculo);
        os.setOperador(operador);
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);
        os.setDtInicioOs(LocalDateTime.now());
        os.setDtFimOs(LocalDateTime.now());
        os.setValorTotalOs(new BigDecimal("0"));
        os.setPecas(new ArrayList<>());
        os.setServicos(new ArrayList<>());

        OrdemDeServico osSalva = ordemRepository.save(os);

        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto adicionarServicoPeca(UUID id, AddServicoPecaOrdemDeServicoDto request) {

        OrdemDeServico os = buscar(id);
        BigDecimal total = BigDecimal.ZERO;

        for (var dto : request.getPecas()) {

            Peca peca = pecaRepository.findById(dto.getPecaId())
                    .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

            pecaService.registrarSaidaEstoque(dto.getPecaId(), dto.getQuantidade());

            PecaOrdemDeServico pos = new PecaOrdemDeServico();
            pos.setOrdemDeServico(os);
            pos.setPeca(peca);
            pos.setQuantidade(dto.getQuantidade());

            total = total.add(
                    peca.getPreco().multiply(BigDecimal.valueOf(dto.getQuantidade())));

            os.getPecas().add(pos);
        }

        for (var dto : request.getServicos()) {

            Servico servico = servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado"));

            ServicoOrdemDeServico sos = new ServicoOrdemDeServico();
            sos.setOrdemDeServico(os);
            sos.setServico(servico);
            sos.setStatus(StatusServicoEnum.AGUARDANDO);

            total = total.add(servico.getPreco());
            os.getServicos().add(sos);
        }

        os.setValorTotalOs(total);
        os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        OrdemDeServico osSalva = ordemRepository.save(os);
        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto iniciarDiagnostico(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.RECEBIDA);
        os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        OrdemDeServico osSalva = ordemRepository.save(os);
        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto aprovarOrcamento(UUID ordemId) {
        OrdemDeServico os = buscar(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

        os.setStatus(StatusOrdemDeServicoEnum.EM_EXECUCAO);

        for (var servico : os.getServicos()) {
            servico.setStatus(StatusServicoEnum.EM_EXECUCAO);
            servico.setDtInicio(LocalDateTime.now());
        }

        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto negarOrcamento(UUID ordemId) {
        OrdemDeServico os = buscar(ordemId);
        validarStatus(os, StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);

        os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);

        for (var servico : os.getServicos()) {
            servico.setStatus(StatusServicoEnum.CANCELADO);
            servico.setDtFim(LocalDateTime.now());
        }

        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto finalizar(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.EM_EXECUCAO);
        os.setStatus(StatusOrdemDeServicoEnum.FINALIZADA);
        os.setDtFimOs(LocalDateTime.now());

        for (var servico : os.getServicos()) {
            servico.setStatus(StatusServicoEnum.FINALIZADO);
            servico.setDtFim(LocalDateTime.now());
        }
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto entregar(UUID id) {
        OrdemDeServico os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.FINALIZADA);
        os.setStatus(StatusOrdemDeServicoEnum.ENTREGUE);
        return mapper.toResponse(ordemRepository.save(os));
    }

    @Override
    public OrdemDeServicoResponseDto buscarPorId(UUID id) {
        return mapper.toResponse(buscar(id));
    }

    @Override
    public List<OrdemDeServicoResponseDto> listar() {
        return ordemRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<TempoMedioServicoResponseDto> calcularTempoMedioServicos() {

        List<OrdemDeServico> ordens = ordemRepository.findByStatus(
                StatusOrdemDeServicoEnum.FINALIZADA);

        Map<String, List<Duration>> temposPorServico = new HashMap<>();

        for (OrdemDeServico os : ordens) {

            for (ServicoOrdemDeServico sos : os.getServicos()) {

                if (sos.getDtInicio() != null
                        && sos.getDtFim() != null) {

                    Duration duracao = Duration.between(
                            sos.getDtInicio(),
                            sos.getDtFim());

                    String descricao = sos.getServico().getDescricao();

                    temposPorServico
                            .computeIfAbsent(descricao, k -> new ArrayList<>())
                            .add(duracao);
                }
            }
        }

        List<TempoMedioServicoResponseDto> response = new ArrayList<>();

        for (var entry : temposPorServico.entrySet()) {

            Duration soma = entry.getValue()
                    .stream()
                    .reduce(Duration.ZERO, Duration::plus);

            Duration media = soma.dividedBy(entry.getValue().size());

            response.add(
                    new TempoMedioServicoResponseDto(
                            entry.getKey(),
                            media.toMinutes()));
        }

        return response;
    }

    private OrdemDeServico buscar(UUID id) {
        return ordemRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("OS não encontrada"));
    }

    private void validarStatus(OrdemDeServico os, StatusOrdemDeServicoEnum esperado) {
        if (os.getStatus() != esperado) {
            throw new RegraNegocioException(
                    "Transição inválida. Status atual: " + os.getStatus());
        }
    }
}