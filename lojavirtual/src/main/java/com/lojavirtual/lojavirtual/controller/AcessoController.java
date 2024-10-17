package com.lojavirtual.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lojavirtual.lojavirtual.ExceptionCustom;
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
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionCustom { //recebe o JSON e converte para objeto
		
		if (acesso.getId() == null) {
			List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());

			if (!acessos.isEmpty()) {
				throw new ExceptionCustom("Já existe Acesso com a descrição: " + acesso.getDescricao());
			}
		}

		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody //Para retorno da API
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso) {
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("Acesso removido com sucesso!", HttpStatus.OK);
	}
	
	//TODO: Remover apenas para teste de Exception.class
	@ResponseBody
	@PostMapping(value = "/testSqlError")
	public ResponseEntity<?> testSqlError() {

	    List<Acesso> acessos = acessoRepository.findInvalidQuery();

	    return new ResponseEntity<List<Acesso>>(acessos, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterAcesso/{id}")
	public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) throws ExceptionCustom { 
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new ExceptionCustom("Não encontrado acesso para id: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
	}


	

}
