package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.FlashCard;

public record ReadFlashCardDTO(
		String term, String definition, String example,
		Long idDeck
		) {

	public ReadFlashCardDTO(FlashCard flashCard) {
		this(flashCard.getTerm(), flashCard.getDefinition(), flashCard.getExample(), flashCard.getDeck().getId());
	}
}
