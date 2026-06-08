package br.com.techchallenge.mecanica.presentation.servico;

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
public class ServicoResponseDto {

    private UUID id;
    private String descricao;
    private BigDecimal preco;

}