package com.renato.flashcards.flashcards_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.flashcards.flashcards_api.domain.Deck;

public interface DeckRepository extends JpaRepository<Deck, Long>{

}
