package com.renato.flashcards.flashcards_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.security.domain.User;

public interface DeckRepository extends JpaRepository<Deck, Long>{

	List<Deck> findAllByOwner(User user);

	List<Deck> findByOwner(User user);
	
	Optional<Deck> findByOwnerAndId(User user, Long id);
	
	Boolean existsByOwnerAndId(User user, Long id);

	void deleteByOwnerAndId(User user, Long id);
}
