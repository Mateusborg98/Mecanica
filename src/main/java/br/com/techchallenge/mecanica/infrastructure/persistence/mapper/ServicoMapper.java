package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServicoMapper {

    public Servico toDomain(ServicoJpaEntity entity) {

        return new Servico(
                entity.getDescricao(),
                entity.getPreco());
    }

    public ServicoJpaEntity toEntity(Servico servico) {

        ServicoJpaEntity entity = new ServicoJpaEntity();

        entity.setId(servico.getId());
        entity.setDescricao(servico.getDescricao());
        entity.setPreco(servico.getPreco());
        entity.setAtivo(servico.isAtivo());

        return entity;
    }
}
