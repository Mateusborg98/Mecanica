package br.com.techchallenge.mecanica.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.infrastructure.security.JwtService;
import br.com.techchallenge.mecanica.presentation.dto.login.CreateLoginRequestDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody CreateLoginRequestDto request) {

        /*
         * MVP TEMPORÁRIO
         */
        if ("admin".equals(request.getUsername())
                && "123".equals(request.getPassword())) {

            /*
             * matrícula do operador mockado
             */
            Integer matricula = 1;

            String token = jwtService.gerarToken(
                    matricula.toString(),
                    "ADMIN");

            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Usuário ou senha inválidos");
    }
}