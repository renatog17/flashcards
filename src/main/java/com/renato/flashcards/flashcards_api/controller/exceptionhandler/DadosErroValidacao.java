package com.renato.flashcards.flashcards_api.controller.exceptionhandler;

import org.springframework.validation.FieldError;

public record DadosErroValidacao(String campo, String mensagem) {

	public DadosErroValidacao(FieldError erro) {
		this(erro.getField(), erro.getDefaultMessage());
	}
}