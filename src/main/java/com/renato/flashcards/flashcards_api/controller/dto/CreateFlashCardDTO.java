package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.domain.FlashCard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFlashCardDTO(
		@NotNull(message = "não deve ser nulo") Long idDeck,
		@NotBlank(message = "não deve estar em branco") String term, 
		@NotBlank(message = "não deve estar em branco") String definition, 
	    @NotBlank(message = "não deve estar em branco") String example) {

	public FlashCard toModel() {
		Deck deck = new Deck(this.idDeck);
		return new FlashCard(this.term, this.definition, this.example, deck);
	}
}
