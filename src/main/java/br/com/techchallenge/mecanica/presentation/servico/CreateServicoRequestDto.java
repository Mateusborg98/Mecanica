package br.com.techchallenge.mecanica.presentation.servico;

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
public class CreateServicoRequestDto {

    @NotBlank
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal preco;
    
}