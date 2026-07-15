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
                .preco(peca.getPreco())
                .ativo(peca.isAtivo())
                .dataInativacao(peca.getDataInativacao())
                .build();
    }

    public Peca toDomain(PecaJpaEntity pecaJpaEntity) {
        return new Peca(pecaJpaEntity.getId(),
                pecaJpaEntity.getNome(),
                pecaJpaEntity.getMarca(),
                pecaJpaEntity.getPreco(),
                pecaJpaEntity.isAtivo(),
                pecaJpaEntity.getDataInativacao());
    }
}
