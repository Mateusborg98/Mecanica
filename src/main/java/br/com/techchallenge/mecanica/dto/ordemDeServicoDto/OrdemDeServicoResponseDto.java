package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServicoResponseDto {

    private UUID id;
    private String status;
    private String cliente;
    private String veiculo;
    private BigDecimal valorTotal;
    private LocalDateTime dtInicioOs;

}