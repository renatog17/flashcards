package com.renato.flashcards.flashcards_api.controller.dto;

import com.renato.flashcards.flashcards_api.domain.Deck;

public record ReadDeckDTO(Long id, String name) {

	public ReadDeckDTO(Deck deck) {
		this(deck.getId(), deck.getName());
	}
}
