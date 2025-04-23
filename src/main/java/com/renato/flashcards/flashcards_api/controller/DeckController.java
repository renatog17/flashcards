package com.renato.flashcards.flashcards_api.controller;

import java.net.URI;
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
import com.renato.flashcards.flashcards_api.repository.DeckRepository;
import com.renato.flashcards.flashcards_api.security.domain.User;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/deck")
@CrossOrigin(origins = "http://localhost:3000")
public class DeckController {

	private DeckRepository deckRepository;

	public DeckController(DeckRepository deckRepository) {
		super();
		this.deckRepository = deckRepository;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createDeck(@RequestBody CreateDeckDTO dto, UriComponentsBuilder uri,
		@AuthenticationPrincipal User user){
		Deck model = dto.toModel();
		model.setOwner(user);
		deckRepository.save(model);
		URI location = uri.path("api/deck/{id}").buildAndExpand(model.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{id}/flashcards")
	@Transactional
	public ResponseEntity<?> readDeckWithFlashCards(@PathVariable Long id){
		Deck deckWithFlashCards = deckRepository.findById(id).orElseThrow();
		return ResponseEntity.ok(new ReadDeckWithFlashCardsDTO(deckWithFlashCards));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> readDeck(@PathVariable Long id) {
		Deck deck = deckRepository.findById(id).orElseThrow();
		return ResponseEntity.ok(new ReadDeckDTO(deck));
	}
	
	@GetMapping
	public ResponseEntity<List<ReadDeckDTO>> getDecks() {
	    List<Deck> decks = deckRepository.findAll();
	    List<ReadDeckDTO> dtoList = decks.stream()
	                                     .map(ReadDeckDTO::new)
	                                     .toList();
	    return ResponseEntity.ok(dtoList);
	}


	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateDeck(@PathVariable Long id, @RequestBody UpdateDeckDTO dto) {
		Deck deck = deckRepository.findById(id).orElseThrow();
		deck.setName(dto.name());
		return ResponseEntity.ok(new ReadDeckDTO(deck));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDeck(@PathVariable Long id) {
		deckRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
