package br.com.techchallenge.mecanica.dto.ordemDeServicoDto;

import br.com.techchallenge.mecanica.annotation.CpfCnpj;
import br.com.techchallenge.mecanica.annotation.PlacaValida;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrdemDeServicoRequestDto {

    @NotBlank
    @CpfCnpj
    private String cpfCnpj;

    @NotBlank
    @PlacaValida
    private String placa;

}