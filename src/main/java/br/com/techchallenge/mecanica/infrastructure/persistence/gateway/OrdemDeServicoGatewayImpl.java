package br.com.techchallenge.mecanica.infrastructure.persistence.gateway;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.techchallenge.mecanica.application.gateway.OrdemDeServicoGateway;
import br.com.techchallenge.mecanica.domain.enums.StatusOrdemDeServicoEnum;
import br.com.techchallenge.mecanica.domain.ordemdeservico.OrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoOrdemDeServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.mapper.OrdemDeServicoMapper;
import br.com.techchallenge.mecanica.infrastructure.persistence.repository.OrdemDeServicoJpaRepository;
import br.com.techchallenge.mecanica.presentation.ordemDeServico.TempoMedioServicoResponseDto;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrdemDeServicoGatewayImpl implements OrdemDeServicoGateway {

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
                .map(ordemDeServicoMapper::toDomain)
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
}