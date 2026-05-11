package br.com.techchallenge.mecanica.dto.itemOrdemDeServicoDto;

import java.math.BigDecimal;
import java.util.UUID;

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
public class CreateItemOrdemDeServicoRequestDto {

    @NotNull
    private UUID pecaId;

    @NotNull
    @Positive
    private Integer quantidade;

    @NotNull
    @Positive
    private BigDecimal valorUnitario;

}