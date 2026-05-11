package br.com.techchallenge.mecanica.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class UsuarioAutenticadoServiceTest {

    private final UsuarioAutenticadoService service =
            new UsuarioAutenticadoService();

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarMatriculaDoUsuarioAutenticado() {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "123",
                        null);

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        Integer matricula = service.getMatricula();

        assertEquals(123, matricula);
    }

    @Test
    void deveRetornarNullQuandoNaoHouverAutenticacao() {

        SecurityContextHolder.clearContext();

        Integer matricula = service.getMatricula();

        assertNull(matricula);
    }
}