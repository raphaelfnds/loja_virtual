package com.lojavirtual.lojavirtual.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lojavirtual.lojavirtual.service.ImplementacaoUserDetailService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {

	@Autowired
	private ImplementacaoUserDetailService userDetailsService;

	@Autowired
	private JWTTokenAutenticacaoService jwtTokenAutenticacaoService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// Configura o AuthenticationManager com o UserDetailsService e o
		// PasswordEncoder
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

		// Obtém o AuthenticationManager
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		// Define o AuthenticationManager no HttpSecurity
		http.authenticationManager(authenticationManager);

		// Cria instâncias dos filtros e define o AuthenticationManager
		JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(jwtTokenAutenticacaoService);
		jwtLoginFilter.setAuthenticationManager(authenticationManager);
		jwtLoginFilter.setFilterProcessesUrl("/login");

		JwtApiAutenticacaoFilter jwtApiAutenticacaoFilter = new JwtApiAutenticacaoFilter(jwtTokenAutenticacaoService);

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/", "/index").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/salvarAcesso", "/deleteAcesso").permitAll()
						.requestMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso").permitAll().anyRequest()
						.authenticated())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/index"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtApiAutenticacaoFilter, UsernamePasswordAuthenticationFilter.class)
				.cors(withDefaults());

		return http.build();
	}
}
