package com.lojavirtual.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lojavirtual.lojavirtual.controller.AcessoController;
import com.lojavirtual.lojavirtual.model.Acesso;

@SpringBootTest(classes = LojavirtualApplication.class)
public class LojavirtualApplicationTests {

	@Autowired
	private AcessoController acessoController;

	@Test
	void testeCadastraAcesso() {

		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");
		acessoController.salvarAcesso(acesso);
	}

}
