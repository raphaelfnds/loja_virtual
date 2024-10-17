package com.lojavirtual.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtApiAutenticacaoFilter extends OncePerRequestFilter {

	private final JWTTokenAutenticacaoService jwtTokenAutenticacaoService;

	public JwtApiAutenticacaoFilter(JWTTokenAutenticacaoService jwtTokenAutenticacaoService) {
		this.jwtTokenAutenticacaoService = jwtTokenAutenticacaoService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		try {
			
			Authentication authentication = jwtTokenAutenticacaoService.getAuthentication(request, response);

			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				chain.doFilter(request, response);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				jwtTokenAutenticacaoService.liberarCors(response);
				response.getWriter().write("{\"error\": \"Erro desconhecido na autenticação.\"}");
			}
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			jwtTokenAutenticacaoService.liberarCors(response);
			response.getWriter().write("{\"error\": \"Token expirado, realize novo login.\"}");
		} catch (SecurityException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			jwtTokenAutenticacaoService.liberarCors(response);
			response.getWriter().write("{\"error\": \"Token inválido.\"}");
		} catch (Exception e) {
		    e.printStackTrace();
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    jwtTokenAutenticacaoService.liberarCors(response);

		    Throwable rootCause = e;
		    while (rootCause.getCause() != null) {
		        rootCause = rootCause.getCause();
		    }
		    String errorMessage = rootCause.getMessage();

		    if (errorMessage == null || errorMessage.trim().isEmpty()) {
		        errorMessage = "Erro interno do servidor, se persistir contate o suporte.";
		        
		    } else {
		        errorMessage = errorMessage.replaceAll("[^\\p{L}\\p{N}\\s]", "");
		        errorMessage = errorMessage.replace("\n", " ").replace("\r", " ").trim().replaceAll(" +", " ");
		    }

		    response.getWriter().write("{\"error\": \"Erro interno do servidor: " + errorMessage + "\"}");
		}

	}
}
