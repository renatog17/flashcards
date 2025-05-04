package com.renato.flashcards.flashcards_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
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
		System.out.println("usuário autenticado "+ userService.getAuthenticatedUser().getLogin());
		if(existsByOwnerAndId(id)) {
			Deck referenceById = deckRepository.getReferenceById(id);
			System.out.println("usuário dono do deck " +referenceById.getOwner().getLogin());
			deckRepository.deleteByOwnerAndId(userService.getAuthenticatedUser(), id);
		}else {
			throw new AccessDeniedException("Não foi possível criar um novo flashcard");
		}
	}
	
	public Boolean existsByOwnerAndId(Long id) {
		return deckRepository.existsByOwnerAndId(userService.getAuthenticatedUser(), id);
	}
}
