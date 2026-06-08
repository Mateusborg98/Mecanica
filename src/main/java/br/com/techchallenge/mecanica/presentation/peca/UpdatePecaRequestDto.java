package br.com.techchallenge.mecanica.presentation.peca;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePecaRequestDto {

    private String nome;
    private String marca;
    private BigDecimal preco;

}