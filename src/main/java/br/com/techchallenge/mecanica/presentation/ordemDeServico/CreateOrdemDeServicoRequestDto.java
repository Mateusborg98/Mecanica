package br.com.techchallenge.mecanica.presentation.ordemDeServico;

import br.com.techchallenge.mecanica.presentation.annotation.CpfCnpjAnnotation;
import br.com.techchallenge.mecanica.presentation.annotation.PlacaValida;
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
    @CpfCnpjAnnotation
    private String cpfCnpj;

    @NotBlank
    @PlacaValida
    private String placa;

}