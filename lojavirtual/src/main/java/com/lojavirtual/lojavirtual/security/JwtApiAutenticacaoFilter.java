package com.lojavirtual.lojavirtual.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication authentication = jwtTokenAutenticacaoService.getAuthentication(request, response);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
