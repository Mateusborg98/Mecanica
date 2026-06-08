package br.com.techchallenge.mecanica.presentation.estoque;

import br.com.techchallenge.mecanica.infrastructure.persistence.entity.PecaJpaEntity;
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

    private PecaJpaEntity pecaJpaEntity;

    @NotNull
    @Positive
    private Integer quantidade;

}