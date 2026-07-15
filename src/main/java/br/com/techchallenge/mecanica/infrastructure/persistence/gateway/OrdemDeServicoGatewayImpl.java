package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.dto.ordemdeservico.TempoMedioServicoOutput;
import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.OrdemDeServicoJpaRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrdemDeServicoGatewayImpl implements OrdemDeServicoGateway {

    private static final Map<StatusOrdemDeServicoEnum, Integer> PRIORIDADE_STATUS = Map.of(
            StatusOrdemDeServicoEnum.EM_EXECUCAO, 1,
            StatusOrdemDeServicoEnum.AGUARDANDO_APROVACAO, 2,
            StatusOrdemDeServicoEnum.EM_DIAGNOSTICO, 3,
            StatusOrdemDeServicoEnum.RECEBIDA, 4);

    private static final Comparator<OrdemDeServicoJpaEntity> ORDENACAO_LISTAGEM = Comparator
            .comparingInt((OrdemDeServicoJpaEntity ordem) -> PRIORIDADE_STATUS.get(ordem.getStatus()))
            .thenComparing(
                    OrdemDeServicoJpaEntity::getDtCriacao,
                    Comparator.nullsLast(Comparator.naturalOrder()));

    private final OrdemDeServicoJpaRepository ordemRepository;
    private final OrdemDeServicoMapper ordemDeServicoMapper;

    @Override
    public OrdemDeServico salvar(OrdemDeServico ordemDeServico) {

        OrdemDeServicoJpaEntity entity = ordemDeServicoMapper.toEntity(ordemDeServico);

        OrdemDeServicoJpaEntity salvo = ordemRepository.save(entity);

        return ordemDeServicoMapper.toDomain(salvo);
    }

    @Override
    public Optional<OrdemDeServico> buscarPorId(UUID id) {
        return ordemRepository.findById(id).map(ordemDeServicoMapper::toDomain);
    }

    @Override
    public List<OrdemDeServico> listar() {
        return ordemRepository.findAll()
                .stream()
                .filter(ordem -> PRIORIDADE_STATUS.containsKey(ordem.getStatus()))
                .sorted(ORDENACAO_LISTAGEM)
                .map(ordemDeServicoMapper::toDomain)
                .toList();
    }

    @Override
    public List<TempoMedioServicoOutput> calcularTempoMedioServicos() {

        List<OrdemDeServicoJpaEntity> ordens = ordemRepository.findByStatus(
                StatusOrdemDeServicoEnum.FINALIZADA);

        Map<UUID, List<Duration>> temposPorServico = new HashMap<>();

        for (OrdemDeServicoJpaEntity os : ordens) {

            for (ServicoOrdemDeServicoJpaEntity sos : os.getServicos()) {

                if (sos.getDtInicio() != null
                        && sos.getDtFim() != null) {

                    Duration duracao = Duration.between(
                            sos.getDtInicio(),
                            sos.getDtFim());

                    UUID servicoId = sos.getServicoJpaEntity().getId();

                    temposPorServico
                            .computeIfAbsent(servicoId, k -> new ArrayList<>())
                            .add(duracao);
                }
            }
        }

        List<TempoMedioServicoOutput> response = new ArrayList<>();

        for (var entry : temposPorServico.entrySet()) {

            long mediaEmMinutos = entry.getValue().stream()
                    .mapToLong(duracao -> duracao.toMinutes())
                    .sum() / entry.getValue().size();

            response.add(
                    new TempoMedioServicoOutput(
                            entry.getKey(),
                            mediaEmMinutos));
        }

        return response;
    }
}
