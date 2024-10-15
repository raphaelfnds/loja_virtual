package com.lojavirtual.lojavirtual.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.lojavirtual.lojavirtual.ApplicationContextLoad;
import com.lojavirtual.lojavirtual.model.Usuario;
import com.lojavirtual.lojavirtual.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTTokenAutenticacaoService {

    private static final long EXPIRATION_TIME = 959990000;

    private static final String SECRET_KEY = "ss/-*-*sds565dsd-s/d-s*dsds12345678901234567890123456789012";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String HEADER_STRING = "Authorization";

    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public void addAuthentication(HttpServletResponse response, String username) throws Exception {

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();

        String fullToken = TOKEN_PREFIX + token;

        response.addHeader(HEADER_STRING, fullToken);

        liberarCors(response);

        response.getWriter().write("{\"Authorization\": \"" + fullToken + "\"}");
    }

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader(HEADER_STRING);

        if (token != null && token.startsWith(TOKEN_PREFIX)) {

            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

            String user = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(tokenLimpo)
                    .getBody()
                    .getSubject();

            if (user != null) {
                Usuario usuario = ApplicationContextLoad
                        .getApplicationContext()
                        .getBean(UsuarioRepository.class)
                        .findByLogin(user);

                if (usuario != null) {
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            null,
                            usuario.getAuthorities());
                }
            }
        }

        liberarCors(response);
        return null;
    }

    private void liberarCors(HttpServletResponse response) {
        if (!response.getHeaderNames().contains("Access-Control-Allow-Origin")) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        if (!response.getHeaderNames().contains("Access-Control-Allow-Headers")) {
            response.addHeader("Access-Control-Allow-Headers", "*");
        }
        if (!response.getHeaderNames().contains("Access-Control-Expose-Headers")) {
            response.addHeader("Access-Control-Expose-Headers", "*");
        }
        if (!response.getHeaderNames().contains("Access-Control-Allow-Methods")) {
            response.addHeader("Access-Control-Allow-Methods", "*");
        }
    }
}
