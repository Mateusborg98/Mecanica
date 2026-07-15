package br.com.techchallenge.mecanica.presentation.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
