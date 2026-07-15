package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.estoque.Estoque;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.EstoqueJpaEntity;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;

@Component
public class EstoqueMapper {

    public EstoqueJpaEntity toJpaEntity(Estoque estoque) {
        PecaJpaEntity pecaJpaEntity = new PecaJpaEntity();
        pecaJpaEntity.setId(estoque.getPecaId());

        return EstoqueJpaEntity.builder()
                .id(estoque.getId())
                .pecaJpaEntity(pecaJpaEntity)
                .quantidade(estoque.getQuantidade())
                .versao(estoque.getVersao())
                .build();
    }

    public Estoque toDomain(EstoqueJpaEntity estoqueJpaEntity) {
        return new Estoque(
                estoqueJpaEntity.getId(),
                estoqueJpaEntity.getPecaJpaEntity().getId(),
                estoqueJpaEntity.getQuantidade(),
                estoqueJpaEntity.getVersao());
    }

}
