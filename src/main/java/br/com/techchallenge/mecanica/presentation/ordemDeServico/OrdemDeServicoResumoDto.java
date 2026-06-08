package br.com.techchallenge.mecanica.presentation.ordemDeServico;

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
public class OrdemDeServicoResumoDto {

    private UUID id;
    private String status;
    private LocalDateTime dtInicioOs;
    private String nomeCliente;
    private String placaVeiculo;

}
