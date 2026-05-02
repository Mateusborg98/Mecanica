package br.com.techchallenge.mecanica.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallenge.mecanica.dto.loginDto.LoginRequestDto;
import br.com.techchallenge.mecanica.security.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto request) {

        // MVP: usuário fixo
        if ("admin".equals(request.getUsername())
                && "123".equals(request.getPassword())) {

            return JwtService.gerarToken(request.getUsername());
        }

        throw new RuntimeException("Usuário ou senha inválidos");
    }
}