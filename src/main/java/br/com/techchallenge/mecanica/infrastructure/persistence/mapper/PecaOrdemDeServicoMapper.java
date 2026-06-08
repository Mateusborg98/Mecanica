package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.domain.pecaordemdeservico.PecaOrdemDeServico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaOrdemDeServicoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PecaOrdemDeServicoMapper {

    private final PecaMapper pecaMapper;

    public PecaOrdemDeServicoJpaEntity toJpaEntity(PecaOrdemDeServico pecaOrdemDeServico) {

        PecaJpaEntity pecaJpaEntity = pecaMapper.toJpaEntity(pecaOrdemDeServico.getPeca());

        return PecaOrdemDeServicoJpaEntity.builder()
                .id(pecaOrdemDeServico.getId())
                .pecaJpaEntity(pecaJpaEntity)
                .quantidade(pecaOrdemDeServico.getQuantidade())
                .build();
    }

    public List<PecaOrdemDeServicoJpaEntity> toListJpaEntity(List<PecaOrdemDeServico> pecaOrdensDeServicos) {

        List<PecaOrdemDeServicoJpaEntity> pecaOrdemDeServicoJpaEntity = new ArrayList<>();

        for (PecaOrdemDeServico pecaOrdemDeServico : pecaOrdensDeServicos) {
            PecaJpaEntity pecaJpaEntity = pecaMapper.toJpaEntity(pecaOrdemDeServico.getPeca());

            pecaOrdemDeServicoJpaEntity.add(PecaOrdemDeServicoJpaEntity.builder()
                    .id(pecaOrdemDeServico.getId())
                    .pecaJpaEntity(pecaJpaEntity)
                    .quantidade(pecaOrdemDeServico.getQuantidade())
                    .build());
        }

        return pecaOrdemDeServicoJpaEntity;
    }

    public PecaOrdemDeServico toDomain(PecaOrdemDeServicoJpaEntity pecaOrdemDeServicoJpaEntity) {

        Peca peca = pecaMapper.toDomain(pecaOrdemDeServicoJpaEntity.getPecaJpaEntity());

        return PecaOrdemDeServico.builder()
                .id(pecaOrdemDeServicoJpaEntity.getId())
                .peca(peca)
                .quantidade(pecaOrdemDeServicoJpaEntity.getQuantidade())
                .build();
    }

    public List<PecaOrdemDeServico> toListDomain(List<PecaOrdemDeServicoJpaEntity> pecaOrdemDeServicoJpaEntity) {

        List<PecaOrdemDeServico> pecaOrdemDeServicos = new ArrayList<>();

        for (PecaOrdemDeServicoJpaEntity pecaOrdemDeServico : pecaOrdemDeServicoJpaEntity) {

            Peca peca = pecaMapper.toDomain(pecaOrdemDeServico.getPecaJpaEntity());

            pecaOrdemDeServicos.add(PecaOrdemDeServico.builder()
                    .id(pecaOrdemDeServico.getId())
                    .peca(peca)
                    .quantidade(pecaOrdemDeServico.getQuantidade())
                    .build());
        }

        return pecaOrdemDeServicos;
    }
}
