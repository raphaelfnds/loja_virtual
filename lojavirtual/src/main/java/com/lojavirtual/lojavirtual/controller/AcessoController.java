package com.lojavirtual.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lojavirtual.lojavirtual.model.Acesso;
import com.lojavirtual.lojavirtual.repository.AcessoRepository;
import com.lojavirtual.lojavirtual.service.AcessoService;

@Controller
@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody //Para retorno da API
	@PostMapping(value = "/salvarAcesso") //Mapeamento ** de qualquer lugar
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) { //recebe o JSON e converte para objeto
		
		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody //Para retorno da API
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("Acesso removido com sucesso!", HttpStatus.OK);
	}
	

}
