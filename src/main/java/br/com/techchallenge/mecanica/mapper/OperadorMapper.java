package br.com.techchallenge.mecanica.mapper;

import org.springframework.stereotype.Component;

import br.com.techchallenge.mecanica.dto.operadorDto.OperadorResumoDto;
import br.com.techchallenge.mecanica.entity.Operador;

@Component
public class OperadorMapper {

    public static OperadorResumoDto toResumo(Operador operador) {
        if (operador == null) {
            return null;
        }

        return new OperadorResumoDto(
            operador.getId(),
            operador.getNome()
        );
    }
}
