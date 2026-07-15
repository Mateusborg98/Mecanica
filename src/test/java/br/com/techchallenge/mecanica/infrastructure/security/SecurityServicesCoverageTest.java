package br.com.techchallenge.mecanica.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class SecurityServicesCoverageTest {

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void jwtServiceDeveGerarExtrairEValidarToken() {
        var service = jwtService();
        var token = service.gerarToken("123", "ADMIN");

        assertEquals("123", service.extrairUsername(token));
        assertEquals("ADMIN", service.extrairRole(token));
        assertTrue(service.tokenValido(token));
        assertFalse(service.tokenValido("token-invalido"));
    }

    @Test
    void usuarioAutenticadoDeveRetornarNuloOuMatricula() {
        var service = new UsuarioAutenticadoService();
        assertNull(service.getMatricula());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("321", null));
        assertEquals(321, service.getMatricula());
    }

    @Test
    void filtroDeveAutenticarBearerValidoEContinuarCadeia() throws Exception {
        var jwt = jwtService();
        var filtro = new JwtAuthenticationFilter(jwt);
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + jwt.gerarToken("456", "ADMIN"));

        filtro.doFilterInternal(request, response, chain);

        assertEquals("456", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(request, response);
    }

    @Test
    void filtroDeveIgnorarTokenInvalidoHeaderAusenteEUsuarioJaAutenticado() throws Exception {
        var jwt = jwtService();
        var filtro = new JwtAuthenticationFilter(jwt);
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalido");
        filtro.doFilterInternal(request, response, chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        when(request.getHeader("Authorization")).thenReturn(null);
        filtro.doFilterInternal(request, response, chain);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("existente", null));
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + jwt.gerarToken("novo", "ADMIN"));
        filtro.doFilterInternal(request, response, chain);
        assertEquals("existente", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void filtroDevePularSomenteRotasPublicasConfiguradas() {
        var filtro = new JwtAuthenticationFilter(jwtService());
        var request = mock(HttpServletRequest.class);

        when(request.getServletPath()).thenReturn("/auth/login", "/swagger-ui/index.html",
                "/v3/api-docs", "/actuator/health", "/clientes", null);
        assertTrue(filtro.shouldNotFilter(request));
        assertTrue(filtro.shouldNotFilter(request));
        assertTrue(filtro.shouldNotFilter(request));
        assertTrue(filtro.shouldNotFilter(request));
        assertFalse(filtro.shouldNotFilter(request));
        assertFalse(filtro.shouldNotFilter(request));
    }

    private JwtService jwtService() {
        var service = new JwtService();
        ReflectionTestUtils.setField(service, "secretKey",
                "chave-de-testes-com-pelo-menos-trinta-e-dois-caracteres");
        return service;
    }
}
