package com.lojavirtual.lojavirtual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lojavirtual.lojavirtual.controller.AcessoController;
import com.lojavirtual.lojavirtual.model.Acesso;

@SpringBootTest
public class LojavirtualApplicationTests {

    @Autowired
    private AcessoController acessoController;

    @Test
    void testeCadastraAcesso() throws ExceptionCustom {
        Acesso acesso = new Acesso();
        acesso.setDescricao("ROLE_ADMIN");
        acesso = acessoController.salvarAcesso(acesso).getBody();
        
        System.out.println("ID gerado: " + acesso.getId());
        
        assertTrue(acesso.getId() > 0);
    }


}
