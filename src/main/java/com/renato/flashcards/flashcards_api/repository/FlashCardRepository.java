package com.renato.flashcards.flashcards_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.flashcards.flashcards_api.domain.FlashCard;

public interface FlashCardRepository extends JpaRepository<FlashCard, Long>{

}
