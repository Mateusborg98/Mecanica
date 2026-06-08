package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

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

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.enums.StatusServicoEnum;
import br.com.techchallenge.mecanica.domain.exception.RegraNegocioException;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ClienteJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.VeiculoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ClienteJpaRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.OperadorRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.OrdemDeServicoRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.PecaRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.ServicoRepository;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.VeiculoJpaRepository;
import br.com.techchallenge.mecanica.infrastructure.security.UsuarioAutenticadoService;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.AddServicoPecaOrdemDeServicoDto;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.CreateOrdemDeServicoRequestDto;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.OrdemDeServicoResponseDto;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.TempoMedioServicoResponseDto;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrdemDeServicoGatewayImpl implements OrdemDeServicoGateway {

    private final OrdemDeServicoRepository ordemRepository;
    private final ClienteJpaRepository clienteJpaRepository;
    private final VeiculoJpaRepository veiculoJpaRepository;
    private final PecaRepository pecaRepository;
    private final ServicoRepository servicoRepository;
    private final OperadorRepository operadorRepository;
    private final PecaGatewayImpl pecaService;
    private final UsuarioAutenticadoService autenticadoService;
    private final OrdemDeServicoMapper mapperOrdemDeServicoMapper;

    @Override
    public OrdemDeServico salvar(OrdemDeServico ordemDeServico) {

        OrdemDeServicoJpaEntity entity = mapperOrdemDeServicoMapper.toEntity(ordemDeServico);

        OrdemDeServicoJpaEntity salvo = ordemRepository.save(entity);

        return mapperOrdemDeServicoMapper.toDomain(salvo);
    }

    @Override
    public OrdemDeServicoResponseDto criar(CreateOrdemDeServicoRequestDto request) {

        ClienteJpaEntity clienteJpaEntity = clienteJpaRepository.findByCpfCnpj(request.getCpfCnpj())
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado"));

        VeiculoJpaEntity veiculoJpaEntity = veiculoJpaRepository.findByPlaca(request.getPlaca())
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado"));

        OperadorJpaEntity operadorJpaEntity = operadorRepository
                .findByMatricula(autenticadoService.getMatricula())
                .orElseThrow(() -> new RegraNegocioException("Operador não encontrado"));

        OrdemDeServicoJpaEntity os = new OrdemDeServicoJpaEntity();
        os.setClienteJpaEntity(clienteJpaEntity);
        os.setVeiculoJpaEntity(veiculoJpaEntity);
        os.setOperadorJpaEntity(operadorJpaEntity);
        os.setStatus(StatusOrdemDeServicoEnum.RECEBIDA);
        os.setDtInicioOs(LocalDateTime.now());
        os.setDtFimOs(LocalDateTime.now());
        os.setValorTotalOs(new BigDecimal("0"));
        os.setPecas(new ArrayList<>());
        os.setServicos(new ArrayList<>());

        OrdemDeServicoJpaEntity osSalva = ordemRepository.save(os);

        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto adicionarServicoPeca(UUID id, AddServicoPecaOrdemDeServicoDto request) {

        OrdemDeServicoJpaEntity os = buscar(id);
        BigDecimal total = BigDecimal.ZERO;

        for (var dto : request.getPecas()) {

            PecaJpaEntity pecaJpaEntity = pecaRepository.findById(dto.getPecaId())
                    .orElseThrow(() -> new RegraNegocioException("Peça não encontrada"));

            pecaService.registrarSaidaEstoque(dto.getPecaId(), dto.getQuantidade());

            PecaOrdemDeServicoJpaEntity pos = new PecaOrdemDeServicoJpaEntity();
            pos.setOrdemDeServicoJpaEntity(os);
            pos.setPecaJpaEntity(pecaJpaEntity);
            pos.setQuantidade(dto.getQuantidade());

            total = total.add(
                    pecaJpaEntity.getPreco().multiply(BigDecimal.valueOf(dto.getQuantidade())));

            os.getPecas().add(pos);
        }

        for (var dto : request.getServicos()) {

            ServicoJpaEntity servicoJpaEntity = servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado"));

            ServicoOrdemDeServicoJpaEntity sos = new ServicoOrdemDeServicoJpaEntity();
            sos.setOrdemDeServicoJpaEntity(os);
            sos.setServicoJpaEntity(servicoJpaEntity);
            sos.setStatus(StatusServicoEnum.AGUARDANDO);

            total = total.add(servicoJpaEntity.getPreco());
            os.getServicos().add(sos);
        }

        os.setValorTotalOs(total);
        os.setStatus(StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO);
        OrdemDeServicoJpaEntity osSalva = ordemRepository.save(os);
        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto iniciarDiagnostico(UUID id) {
        OrdemDeServicoJpaEntity os = buscar(id);
        validarStatus(os, StatusOrdemDeServicoEnum.RECEBIDA);
        os.setStatus(StatusOrdemDeServicoEnum.EM_DIAGNOSTICO);
        OrdemDeServicoJpaEntity osSalva = ordemRepository.save(os);
        return mapper.toResponse(osSalva);
    }

    @Override
    @Transactional
    public OrdemDeServicoResponseDto aprovarOrcamento(UUID ordemId) {
        OrdemDeServicoJpaEntity os = buscar(ordemId);
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
        OrdemDeServicoJpaEntity os = buscar(ordemId);
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
        OrdemDeServicoJpaEntity os = buscar(id);
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
        OrdemDeServicoJpaEntity os = buscar(id);
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

        List<OrdemDeServicoJpaEntity> ordens = ordemRepository.findByStatus(
                StatusOrdemDeServicoEnum.FINALIZADA);

        Map<String, List<Duration>> temposPorServico = new HashMap<>();

        for (OrdemDeServicoJpaEntity os : ordens) {

            for (ServicoOrdemDeServicoJpaEntity sos : os.getServicos()) {

                if (sos.getDtInicio() != null
                        && sos.getDtFim() != null) {

                    Duration duracao = Duration.between(
                            sos.getDtInicio(),
                            sos.getDtFim());

                    String descricao = sos.getServicoJpaEntity().getDescricao();

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

    private OrdemDeServicoJpaEntity buscar(UUID id) {
        return ordemRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("OS não encontrada"));
    }

    private void validarStatus(OrdemDeServicoJpaEntity os, StatusOrdemDeServicoEnum esperado) {
        if (os.getStatus() != esperado) {
            throw new RegraNegocioException(
                    "Transição inválida. Status atual: " + os.getStatus());
        }
    }

    @Override
    public OrdemDeServico criar(OrdemDeServico ordemDeServico) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criar'");
    }

    @Override
    public OrdemDeServico adicionarServicoPeca(UUID id, OrdemDeServico ordemDeServico) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarServicoPeca'");
    }
}