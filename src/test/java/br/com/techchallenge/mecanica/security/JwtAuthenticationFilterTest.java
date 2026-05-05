package br.com.techchallenge.mecanica.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private JwtAuthenticationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtService = mock(JwtService.class);
        filter = new JwtAuthenticationFilter(jwtService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    // ======================================================
    // Sem Authorization header
    // ======================================================

    @Test
    void deveIgnorarRequisicaoSemAuthorizationHeader() throws ServletException, IOException {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    // ======================================================
    // Header inválido
    // ======================================================

    @Test
    void deveIgnorarHeaderAuthorizationInvalido() throws ServletException, IOException {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic abcdef");

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }

    // ======================================================
    // Token válido
    // ======================================================

    @Test
    void deveCriarAutenticacaoQuandoTokenForValido() throws ServletException, IOException {

        String token = "token.jwt.valido";
        String username = "usuarioTeste";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtService.tokenValido(token))
                .thenReturn(true);
        when(jwtService.extrairUsername(token))
                .thenReturn(username);

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());

        verify(filterChain).doFilter(request, response);
    }

    // ======================================================
    // Token inválido
    // ======================================================

    @Test
    void naoDeveCriarAutenticacaoQuandoTokenForInvalido() throws ServletException, IOException {

        String token = "token.jwt.invalido";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtService.tokenValido(token))
                .thenReturn(false);

        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
        verify(filterChain).doFilter(request, response);
    }
}