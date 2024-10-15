package com.lojavirtual.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojavirtual.lojavirtual.model.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

	private final JWTTokenAutenticacaoService jwtTokenAutenticacaoService;

	public JWTLoginFilter(JWTTokenAutenticacaoService jwtTokenAutenticacaoService) {
		this.jwtTokenAutenticacaoService = jwtTokenAutenticacaoService;
		setFilterProcessesUrl("/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getLogin(),
					user.getSenha(), user.getAuthorities());

			return getAuthenticationManager().authenticate(authToken);

		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuário", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		try {
			jwtTokenAutenticacaoService.addAuthentication(response, authResult.getName());

		} catch (Exception e) {
			throw new RuntimeException("Falha ao gerar token JWT", e);
		}
	}
}
