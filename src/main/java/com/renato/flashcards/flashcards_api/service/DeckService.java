package com.renato.flashcards.flashcards_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renato.flashcards.flashcards_api.controller.dto.CreateDeckDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadDeckDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadDeckWithFlashCardsDTO;
import com.renato.flashcards.flashcards_api.controller.dto.UpdateDeckDTO;
import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.repository.DeckRepository;

@Service
public class DeckService {

	private DeckRepository deckRepository;
	private UserService userService;
	
	public DeckService(DeckRepository deckRepository, UserService userService) {
		super();
		this.deckRepository = deckRepository;
		this.userService = userService;
	}
	
	public Deck createDeck(CreateDeckDTO dto) {
		Deck model = dto.toModel();
		model.setOwner(userService.getAuthenticatedUser());
		deckRepository.save(model);
		return model;
	}
	
	public List<ReadDeckWithFlashCardsDTO> readDecksWithFlashCards(){
		List<Deck> deckWithFlashCards = deckRepository.findByOwner(userService.getAuthenticatedUser());
		//remover a linha acima
		List<ReadDeckWithFlashCardsDTO> decks = deckWithFlashCards.stream()
				.map(ReadDeckWithFlashCardsDTO::new)
				.collect(Collectors.toList());
		return decks;
	}
	
	public List<ReadDeckDTO> readDecks(){
		List<Deck> decks = deckRepository.findAllByOwner(userService.getAuthenticatedUser());
		List<ReadDeckDTO> dtoList = decks.stream()
				.map(ReadDeckDTO::new)
				.toList();
		return dtoList;
	}
	
	public Deck readDeck(Long id) {
		return deckRepository.getReferenceById(id);
	}
	@Transactional
	public ReadDeckDTO updateDeck(Long id, UpdateDeckDTO dto) {
		Deck deck = deckRepository.findByOwnerAndId(userService.getAuthenticatedUser(), id).orElseThrow();
		deck.setName(dto.name());
		return new ReadDeckDTO(deck);
	}
	
	@Transactional
	public void deleteDeck(Long id) {
		deckRepository.deleteByOwnerAndId(userService.getAuthenticatedUser(), id);
	}
	
	public Boolean existsByOwnerAndId(Long id) {
		return deckRepository.existsByOwnerAndId(userService.getAuthenticatedUser(), id);
	}
}
