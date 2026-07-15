package br.com.techchallenge.mecanica.presentation.dto.peca;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
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
public class CreatePecaRequestDto {

    @NotBlank
    private String nome;

    @NotBlank
    private String marca;

    @NotNull
    @Positive
    private BigDecimal preco;

    @NotNull
    @Positive
    private Integer quantidadeInicial;

}