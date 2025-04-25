package com.renato.flashcards.flashcards_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.flashcards.flashcards_api.controller.dto.CreateDeckDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadDeckDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadDeckWithFlashCardsDTO;
import com.renato.flashcards.flashcards_api.controller.dto.UpdateDeckDTO;
import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.security.domain.User;
import com.renato.flashcards.flashcards_api.service.DeckService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/deck")
@CrossOrigin(origins = "http://localhost:3000")
public class DeckController {

	private DeckService deckService;

	public DeckController(DeckService deckService) {
		super();
		this.deckService = deckService;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createDeck(@RequestBody CreateDeckDTO dto, UriComponentsBuilder uri){
		Deck deck = deckService.createDeck(dto);
		return ResponseEntity.created(uri.path("api/deck/{id}").buildAndExpand(deck.getId()).toUri()).build();
	}

	@GetMapping("/flashcards")
	@Transactional
	public ResponseEntity<?> readDecksWithFlashCards(@AuthenticationPrincipal User user){
		List<ReadDeckWithFlashCardsDTO> decks = deckService.readDecksWithFlashCards();
		return ResponseEntity.ok(decks);
	}
	
	@GetMapping
	public ResponseEntity<List<ReadDeckDTO>> readDecks(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(deckService.readDecks());
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateDeck(@PathVariable Long id, @RequestBody UpdateDeckDTO dto) {		
		return ResponseEntity.ok(deckService.updateDeck(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDeck(@PathVariable Long id) {
		deckService.deleteDeck(id);
		return ResponseEntity.noContent().build();
	}
}
