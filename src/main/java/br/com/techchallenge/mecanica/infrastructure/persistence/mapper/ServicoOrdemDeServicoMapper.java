package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.domain.servicoordemdeservico.ServicoOrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoOrdemDeServicoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServicoOrdemDeServicoMapper {

    private final ServicoMapper servicoMapper;

    public ServicoOrdemDeServicoJpaEntity toJpaEntity(ServicoOrdemDeServico servicoOrdemDeServico) {

        ServicoJpaEntity servicoJpaEntity = servicoMapper.toJpaEntity(servicoOrdemDeServico.getServico());

        return ServicoOrdemDeServicoJpaEntity.builder()
                .id(servicoOrdemDeServico.getId())
                .servicoJpaEntity(servicoJpaEntity)
                .status(servicoOrdemDeServico.getStatus())
                .dtInicio(servicoOrdemDeServico.getDtInicio())
                .dtFim(servicoOrdemDeServico.getDtFim())
                .build();
    }

    public List<ServicoOrdemDeServicoJpaEntity> toListJpaEntity(List<ServicoOrdemDeServico> servicoOrdensDeServicos) {

        List<ServicoOrdemDeServicoJpaEntity> servicoOrdemDeServicoJpaEntity = new ArrayList<>();

        for (ServicoOrdemDeServico servicoOrdemDeServico : servicoOrdensDeServicos) {
            ServicoJpaEntity servicoJpaEntity = servicoMapper.toJpaEntity(servicoOrdemDeServico.getServico());

            servicoOrdemDeServicoJpaEntity.add(ServicoOrdemDeServicoJpaEntity.builder()
                    .id(servicoOrdemDeServico.getId())
                    .servicoJpaEntity(servicoJpaEntity)
                    .status(servicoOrdemDeServico.getStatus())
                    .dtInicio(servicoOrdemDeServico.getDtInicio())
                    .dtFim(servicoOrdemDeServico.getDtFim())
                    .build());
        }

        return servicoOrdemDeServicoJpaEntity;
    }

    public ServicoOrdemDeServico toDomain(ServicoOrdemDeServicoJpaEntity servicoOrdemDeServicoJpaEntity) {

        Servico servico = servicoMapper.toDomain(servicoOrdemDeServicoJpaEntity.getServicoJpaEntity());

        return ServicoOrdemDeServico.builder()
                .id(servicoOrdemDeServicoJpaEntity.getId())
                .servico(servico)
                .status(servicoOrdemDeServicoJpaEntity.getStatus())
                .dtInicio(servicoOrdemDeServicoJpaEntity.getDtInicio())
                .dtFim(servicoOrdemDeServicoJpaEntity.getDtFim())
                .build();
    }

    public List<ServicoOrdemDeServico> toListDomain(List<ServicoOrdemDeServicoJpaEntity> servicoOrdemDeServicoJpaEntities) {

        List<ServicoOrdemDeServico> servicoOrdemDeServicos = new ArrayList<>();

        for (ServicoOrdemDeServicoJpaEntity servicoOrdemDeServicoJpaEntity : servicoOrdemDeServicoJpaEntities) {
            Servico servico = servicoMapper.toDomain(servicoOrdemDeServicoJpaEntity.getServicoJpaEntity());

            servicoOrdemDeServicos.add(ServicoOrdemDeServico.builder()
                .id(servicoOrdemDeServicoJpaEntity.getId())
                .servico(servico)
                .status(servicoOrdemDeServicoJpaEntity.getStatus())
                .dtInicio(servicoOrdemDeServicoJpaEntity.getDtInicio())
                .dtFim(servicoOrdemDeServicoJpaEntity.getDtFim())
                .build());
        }
        
        return servicoOrdemDeServicos;
    }
}
