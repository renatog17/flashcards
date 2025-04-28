package com.renato.flashcards.flashcards_api.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.renato.flashcards.flashcards_api.controller.dto.CreateFlashCardDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadFlashCardDTO;
import com.renato.flashcards.flashcards_api.controller.dto.UpdateFlashCardDTO;
import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.repository.FlashCardRepository;

@Service
public class FlashCardService {

	private FlashCardRepository flashCardRepository;
	private DeckService deckService;

	public FlashCardService(FlashCardRepository flashCardRepository, DeckService deckService) {
		super();
		this.flashCardRepository = flashCardRepository;
		this.deckService = deckService;
	}
	
	@Transactional
	public FlashCard createFlashCard(CreateFlashCardDTO dto) {
		if(deckService.existsByOwnerAndId(dto.idDeck())	&& 
				!flashCardRepository.existsByDeckAndTermAndDefinition(deckService.readDeck(dto.idDeck()), dto.term(), dto.definition()
		)) {
			FlashCard model = dto.toModel();
			flashCardRepository.save(model);
			return model;
		}else {
			throw new AccessDeniedException("Não foi possível criar um novo flashcard");
		}
	}
	
	@Transactional
	public ReadFlashCardDTO updateFlashCard(UpdateFlashCardDTO dto, Long id) {
		if(deckService.existsByOwnerAndId(id)) {
			FlashCard model = flashCardRepository.getReferenceById(id);
			model.atualizar(dto);
			return new ReadFlashCardDTO(model);
		}else {
			throw new AccessDeniedException("Usuário não tem permissão para adicionar flashcard em um deck que não é seu");
		}
	}
	
	@DeleteMapping
	public void deleteFlashCard(Long id) {
		if(deckService.existsByOwnerAndId(id)) {
			flashCardRepository.deleteById(id);
		}else {
			throw new AccessDeniedException("Usuário não tem permissão para adicionar flashcard em um deck que não é seu");
		}
	}
}
