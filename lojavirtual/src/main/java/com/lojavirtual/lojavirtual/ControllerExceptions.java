package com.lojavirtual.lojavirtual;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lojavirtual.lojavirtual.model.dto.ObjetoErroDTO;

//TODO: Verificar a necessidade e se está funcionando (metodo nao testado)
@ControllerAdvice
@RestControllerAdvice
public class ControllerExceptions extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ Exception.class, RuntimeException.class, Throwable.class })
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
			
		} else {
			msg = ex.getMessage();
		}
		
		ex.printStackTrace();

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(statusCode.value() + " ==> " + ((HttpStatus) statusCode).getReasonPhrase());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class, SQLGrammarException.class })
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		 if (ex instanceof DataIntegrityViolationException) {
	            msg = "Erro de integridade: " + ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();

	        } else if (ex instanceof ConstraintViolationException) {
	            msg = "Erro de chave estrangeira: " + ((ConstraintViolationException) ex).getCause().getCause().getMessage();

	        } else if (ex instanceof SQLGrammarException) {
	            msg = "Erro de SQL (sintaxe ou mapeamento): " + ((SQLGrammarException) ex).getCause().getCause().getMessage();

	        } else if (ex instanceof SQLException) {
	            msg = "Erro de SQL: " + ((SQLException) ex).getCause().getCause().getMessage();

	        } else {
	            msg = ex.getMessage();
	        }
		
		ex.printStackTrace();

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler({ ExceptionCustom.class })
	public ResponseEntity<Object> handleExceptionCustom(ExceptionCustom ex) {

		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		objetoErroDTO.setError(ex.getMessage());
		objetoErroDTO.setCode(HttpStatus.OK.toString());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);

	}
	
}
