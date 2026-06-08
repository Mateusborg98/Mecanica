package br.com.techchallenge.mecanica.presentation.peca;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PecaResumoDto {

    private UUID id;
    private String nome;
    private String marca;
    private BigDecimal preco;

}