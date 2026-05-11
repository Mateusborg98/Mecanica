package br.com.techchallenge.mecanica.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveIgnorarRequisicaoSemAuthorizationHeader()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication());
    }

    @Test
    void deveIgnorarHeaderAuthorizationInvalido()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        request.addHeader(
                "Authorization",
                "Basic 123");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication());
    }

    @Test
    void deveCriarAutenticacaoQuandoTokenForValido()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        request.addHeader(
                "Authorization",
                "Bearer token-valido");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        when(jwtService.extrairUsername("token-valido"))
                .thenReturn("123");

        when(jwtService.tokenValido("token-valido"))
                .thenReturn(true);

        filter.doFilter(request, response, filterChain);

        assertNotNull(
                SecurityContextHolder.getContext()
                        .getAuthentication());

        assertEquals(
                "123",
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName());
    }

    @Test
    void naoDeveCriarAutenticacaoQuandoTokenForInvalido()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        request.addHeader(
                "Authorization",
                "Bearer token-invalido");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        when(jwtService.extrairUsername("token-invalido"))
                .thenReturn("123");

        when(jwtService.tokenValido("token-invalido"))
                .thenReturn(false);

        filter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication());
    }

    @Test
    void deveIgnorarExcecaoQuandoTokenFalhar()
            throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        request.addHeader(
                "Authorization",
                "Bearer token");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        MockFilterChain filterChain =
                new MockFilterChain();

        doThrow(new RuntimeException("erro"))
                .when(jwtService)
                .extrairUsername("token");

        assertDoesNotThrow(() ->
                filter.doFilter(request, response, filterChain));

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication());
    }

    @Test
    void deveRetornarTrueQuandoPathForAuth() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/auth/login");

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void deveRetornarTrueQuandoPathForSwagger() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/swagger-ui/index.html");

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void deveRetornarTrueQuandoPathForApiDocs() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/v3/api-docs");

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void deveRetornarFalseQuandoPathNaoForIgnorado() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setServletPath("/clientes");

        assertFalse(filter.shouldNotFilter(request));
    }

    @Test
    void deveRetornarFalseQuandoPathForNull() {

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        assertFalse(filter.shouldNotFilter(request));
    }
}