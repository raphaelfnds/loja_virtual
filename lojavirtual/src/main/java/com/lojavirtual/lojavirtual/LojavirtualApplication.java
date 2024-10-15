package com.lojavirtual.lojavirtual;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.lojavirtual.lojavirtual.model")
@ComponentScan(basePackages = {"com.lojavirtual.lojavirtual"})
@EnableJpaRepositories(basePackages = {"com.lojavirtual.lojavirtual.repository"})
@EnableTransactionManagement
public class LojavirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojavirtualApplication.class, args);
	}

	@Bean
    public CommandLineRunner run(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("Senha criptografada: " + encodedPassword);
        };
    }
	
}
