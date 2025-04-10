package com.renato.flashcards.flashcards_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renato.flashcards.flashcards_api.controller.dto.NewDeckComFlashCardsDTO;
import com.renato.flashcards.flashcards_api.controller.dto.NewDeckDTO;
import com.renato.flashcards.flashcards_api.controller.dto.NewFlashCardDTO;
import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.service.DeckService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/deck")
public class DeckController {

	private DeckService deckService;

	public DeckController(DeckService deckService) {
		super();
		this.deckService = deckService;
	}

	@PostMapping
	public ResponseEntity<?> createDeck(@RequestBody @Valid NewDeckDTO newDeckDTO){
		return null;
	}
}
