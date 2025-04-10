package com.renato.flashcards.flashcards_api.controller.dto;

import java.util.List;

public record NewDeckComFlashCardsDTO(
		String name,
		List<NewFlashCardDTO> flashCards) {
}
