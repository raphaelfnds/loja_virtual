package com.lojavirtual.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lojavirtual.lojavirtual.model.Acesso;
import com.lojavirtual.lojavirtual.service.AcessoService;

@Controller
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	public Acesso salvarAcesso(Acesso acesso) {
		
		return acessoService.save(acesso);
	}
	

}
