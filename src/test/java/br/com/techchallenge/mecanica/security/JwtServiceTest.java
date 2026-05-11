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

    @Test
    void deveGerarToken() {

        String token =
                jwtService.gerarToken("admin", "ADMIN");

        assertNotNull(token);
    }

    @Test
    void deveExtrairUsername() {

        String token =
                jwtService.gerarToken("admin", "ADMIN");

        String username =
                jwtService.extrairUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void deveExtrairRole() {

        String token =
                jwtService.gerarToken("admin", "ADMIN");

        String role =
                jwtService.extrairRole(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void deveValidarToken() {

        String token =
                jwtService.gerarToken("admin", "ADMIN");

        boolean valido =
                jwtService.tokenValido(token);

        assertTrue(valido);
    }

    @Test
    void deveRetornarFalseParaTokenInvalido() {

        boolean valido =
                jwtService.tokenValido("token-invalido");

        assertFalse(valido);
    }
}