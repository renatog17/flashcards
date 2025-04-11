package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.Deck;

import jakarta.validation.constraints.NotBlank;

public record CreateDeckDTO(
		@NotBlank
		String name) {

	public Deck toModel() {
		return new Deck(this.name);
	}
}
