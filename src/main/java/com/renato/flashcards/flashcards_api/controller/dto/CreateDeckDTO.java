package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.Deck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDeckDTO(
		
		@NotBlank(message = "não deve estar em branco")
		@Size(max = 50, message = "deve ter no máximo {max} caracteres")
		String name) {

	public Deck toModel() {
		return new Deck(this.name);
	}
}
