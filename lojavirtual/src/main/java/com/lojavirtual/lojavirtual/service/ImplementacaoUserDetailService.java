package com.lojavirtual.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lojavirtual.lojavirtual.model.Usuario;
import com.lojavirtual.lojavirtual.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Busca o usuário no banco de dados pelo login
		Usuario usuario = repository.findByLogin(username);

		// Verifica se o usuário existe
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado com o login: " + username);
		}

		// Retorna o usuário que implementa UserDetails
		return usuario;
	}
}
