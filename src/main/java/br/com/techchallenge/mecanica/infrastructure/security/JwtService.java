package br.com.techchallenge.mecanica.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    /*
     * IMPORTANTE:
     * Em produção usar variável de ambiente.
     *
     * Precisa ter pelo menos 32 caracteres.
     */
    private static final String SECRET_KEY =
            "techchallenge-secret-key-jwt-super-segura-2026";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1h

    public String gerarToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey())
                .compact();
    }

    public String extrairUsername(String token) {

        return extrairClaims(token).getSubject();
    }

    public String extrairRole(String token) {

        return extrairClaims(token).get("role", String.class);
    }

    public boolean tokenValido(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    private Claims extrairClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getKey() {

        byte[] keyBytes =
                SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}