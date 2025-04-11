package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.domain.FlashCard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateFlashCardDTO(
		@NotEmpty Long idDeck,
		@NotBlank String term, 
		@NotBlank String definition, 
	    @NotBlank String example) {

	public FlashCard toModel() {
		Deck deck = new Deck(this.idDeck);
		return new FlashCard(this.term, this.definition, this.example, deck);
	}
}
