package br.com.techchallenge.mecanica.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
    }

    // ==============================
    // gerarToken
    // ==============================

    @Test
    void deveGerarTokenComUsername() {
        String token = JwtService.gerarToken("usuarioTeste");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    // ==============================
    // extrairUsername
    // ==============================

    @Test
    void deveExtrairUsernameDoToken() {
        String username = "usuarioTeste";
        String token = JwtService.gerarToken(username);

        String usernameExtraido = jwtService.extrairUsername(token);

        assertEquals(username, usernameExtraido);
    }

    // ==============================
    // tokenValido
    // ==============================

    @Test
    void deveRetornarTrueParaTokenValido() {
        String token = JwtService.gerarToken("usuarioTeste");

        boolean valido = jwtService.tokenValido(token);

        assertTrue(valido);
    }

    @Test
    void deveRetornarFalseParaTokenInvalido() {
        String tokenInvalido = "token.invalido.qualquer";

        boolean valido = jwtService.tokenValido(tokenInvalido);

        assertFalse(valido);
    }

    @Test
    void deveRetornarFalseParaTokenExpiradoOuCorrompido() {
        String tokenCorrompido = JwtService.gerarToken("usuarioTeste") + "abc";

        boolean valido = jwtService.tokenValido(tokenCorrompido);

        assertFalse(valido);
    }
}