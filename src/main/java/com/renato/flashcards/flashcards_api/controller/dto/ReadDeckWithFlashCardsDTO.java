package com.renato.flashcards.flashcards_api.controller.dto;

import java.util.List;

import com.renato.flashcards.flashcards_api.domain.Deck;

public record ReadDeckWithFlashCardsDTO(Long id, String name, List<ReadFlashCardDTO> flashCards) {

	public ReadDeckWithFlashCardsDTO(Deck deck) {
		
		this(deck.getId(), deck.getName(), deck.getFlashCards()
				.stream()
				.map(ReadFlashCardDTO::new)
				.toList());
	}
}
