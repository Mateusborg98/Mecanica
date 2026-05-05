package br.com.techchallenge.mecanica.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

class PlacaValidatorTest {

    private PlacaValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        validator = new PlacaValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    // ==========================
    // Placas válidas
    // ==========================

    @Test
    void deveAceitarPlacaAntigaValida() {
        // Ex: ABC1234
        assertTrue(validator.isValid("ABC1234", context));
    }

    @Test
    void deveAceitarPlacaMercosulValida() {
        // Ex: ABC1D23
        assertTrue(validator.isValid("ABC1D23", context));
    }

    @Test
    void deveAceitarPlacaComCaracteresEspeciais() {
        // normalização: abc-1d23 -> ABC1D23
        assertTrue(validator.isValid("abc-1d23", context));
    }

    // ==========================
    // Placas inválidas
    // ==========================

    @Test
    void naoDeveAceitarPlacaComFormatoInvalido() {
        assertFalse(validator.isValid("AB123CD", context));
    }

    @Test
    void naoDeveAceitarPlacaMuitoCurta() {
        assertFalse(validator.isValid("ABC123", context));
    }

    // ==========================
    // Casos neutros
    // ==========================

    @Test
    void deveAceitarValorNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void deveAceitarValorEmBranco() {
        assertTrue(validator.isValid("   ", context));
    }
}