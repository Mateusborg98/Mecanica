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
                entity.getId(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isAtivo(),
                entity.getDataInativacao());
    }

    public ServicoJpaEntity toEntity(Servico servico) {

        ServicoJpaEntity entity = new ServicoJpaEntity();

        entity.setId(servico.getId());
        entity.setDescricao(servico.getDescricao());
        entity.setPreco(servico.getPreco());
        entity.setAtivo(servico.isAtivo());
        entity.setDataInativacao(servico.getDataInativacao());

        return entity;
    }
}
