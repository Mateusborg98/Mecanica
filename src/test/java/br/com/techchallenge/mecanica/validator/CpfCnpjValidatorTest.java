package br.com.techchallenge.mecanica.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

class CpfCnpjValidatorTest {

    private CpfCnpjValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        validator = new CpfCnpjValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    // ==============================
    // CPF
    // ==============================

    @Test
    void deveAceitarCpfValido() {
        // CPF válido: 529.982.247-25
        boolean valido = validator.isValid("52998224725", context);
        assertTrue(valido);
    }

    @Test
    void naoDeveAceitarCpfInvalido() {
        boolean valido = validator.isValid("11111111111", context);
        assertFalse(valido);
    }

    // ==============================
    // CNPJ
    // ==============================

    @Test
    void deveAceitarCnpjValido() {
        // CNPJ válido: 11.444.777/0001-61
        boolean valido = validator.isValid("11444777000161", context);
        assertTrue(valido);
    }

    @Test
    void naoDeveAceitarCnpjInvalido() {
        boolean valido = validator.isValid("99999999999999", context);
        assertFalse(valido);
    }

    // ==============================
    // Casos gerais
    // ==============================

    @Test
    void naoDeveAceitarDocumentoComTamanhoInvalido() {
        boolean valido = validator.isValid("123", context);
        assertFalse(valido);
    }

    @Test
    void deveAceitarValorNull() {
        boolean valido = validator.isValid(null, context);
        assertTrue(valido);
    }

    @Test
    void deveAceitarCpfValidoComDigitoZero() {
        boolean valido = validator.isValid("16899535009", context);
        assertTrue(valido);
    }

    @Test
    void naoDeveAceitarCpfComCaracteresInvalidos() {
        boolean valido = validator.isValid("12345678AB9", context);
        assertFalse(valido);
    }

    @Test
    void deveRejeitarCpfComSegundoDigitoInvalido() {
        boolean valido = validator.isValid("52998224724", context);
        assertFalse(valido);
    }

}