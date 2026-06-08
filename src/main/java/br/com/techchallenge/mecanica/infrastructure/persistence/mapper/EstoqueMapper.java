package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EstoqueMapper {

    public EstoqueJpaEntity toJpaEntity(Estoque estoque) {
        PecaJpaEntity pecaJpaEntity = new PecaJpaEntity();
        pecaJpaEntity.setId(estoque.getPecaId());

        return EstoqueJpaEntity.builder()
                .id(estoque.getId())
                .pecaJpaEntity(pecaJpaEntity)
                .quantidade(estoque.getQuantidade())
                .build();
    }

    public Estoque toDomain(EstoqueJpaEntity estoqueJpaEntity) {
        return Estoque.builder()
                .id(estoqueJpaEntity.getId())
                .pecaId(estoqueJpaEntity.getPecaJpaEntity().getId())
                .quantidade(estoqueJpaEntity.getQuantidade())
                .build();
    }

}
