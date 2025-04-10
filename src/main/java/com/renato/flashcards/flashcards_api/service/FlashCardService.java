package com.renato.flashcards.flashcards_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.repository.FlashCardRepository;

@Service
public class FlashCardService {

	private FlashCardRepository flashCardRepository;

	public FlashCardService(FlashCardRepository flashCardRepository) {
		super();
		this.flashCardRepository = flashCardRepository;
	}

	public void salvarFlashCard(List<FlashCard> flashCards) {
		flashCardRepository.saveAll(flashCards);
	}

	public void salvarFlashCard(FlashCard flashCard) {
		flashCardRepository.save(flashCard);
	}
}
