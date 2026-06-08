package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.servico.Servico;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.ServicoJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServicoMapper {

    public ServicoJpaEntity toJpaEntity(Servico servico) {
        return ServicoJpaEntity.builder()
                .id(servico.getId())
                .descricao(servico.getDescricao())
                .preco(servico.getPreco())
                .build();
    }

    public Servico toDomain(ServicoJpaEntity servicoJpaEntity) {
        return Servico.builder()
                .id(servicoJpaEntity.getId())
                .descricao(servicoJpaEntity.getDescricao())
                .preco(servicoJpaEntity.getPreco())
                .build();
    }
}
