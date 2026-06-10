package br.com.techchallenge.mecanica.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.domain.operador.Operador;
import br.com.techchallenge.mecanica.infrastructure.persistence.entity.OperadorJpaEntity;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OperadorMapper {

    public OperadorJpaEntity toJpaEntity(Operador operador) {

        return OperadorJpaEntity.builder()
                .id(operador.getId())
                .nome(operador.getNome())
                .matricula(operador.getMatricula())
                .contato(operador.getContato())
                .email(operador.getEmail())
                .build();
    }

    public Operador toDomain(OperadorJpaEntity operadorJpaEntity) {

        return Operador.builder()
                .id(operadorJpaEntity.getId())
                .nome(operadorJpaEntity.getNome())
                .matricula(operadorJpaEntity.getMatricula())
                .contato(operadorJpaEntity.getContato())
                .email(operadorJpaEntity.getEmail())
                .build();
    }

}
