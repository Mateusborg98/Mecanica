package br.com.techchallenge.mecanica.dto.estoqueDto;

import br.com.techchallenge.mecanica.entity.Peca;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEstoqueRequestDto {

    private Peca peca;

    @NotNull
    @Positive
    private Integer quantidade;

}