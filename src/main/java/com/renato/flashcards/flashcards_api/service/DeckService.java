package com.renato.flashcards.flashcards_api.service;

import org.springframework.stereotype.Service;

import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.repository.DeckRepository;

@Service
public class DeckService {

	private DeckRepository deckRepository;

	public DeckService(DeckRepository deckRepository) {
		super();
		this.deckRepository = deckRepository;
	}
	
	public void saveDeck(Deck deck) {
		deckRepository.save(deck);
	}
}
