package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.peca.Peca;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PecaMapper {

    public PecaJpaEntity toJpaEntity(Peca peca) {
        return PecaJpaEntity.builder()
                .id(peca.getId())
                .nome(peca.getNome())
                .marca(peca.getMarca())
                .preco(peca.getPreco()).build();
    }

    public Peca toDomain(PecaJpaEntity pecaJpaEntity) {
        return Peca.builder()
                .id(pecaJpaEntity.getId())
                .nome(pecaJpaEntity.getNome())
                .marca(pecaJpaEntity.getMarca())
                .preco(pecaJpaEntity.getPreco()).build();
    }
}
