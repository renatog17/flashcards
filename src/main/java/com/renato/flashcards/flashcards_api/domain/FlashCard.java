package com.renato.flashcards.flashcards_api.domain;

import com.renato.flashcards.flashcards_api.controller.dto.UpdateFlashCardDTO;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flashcards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FlashCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false, length = 255)
	private String term; // Palavra ou frase

	@Column(nullable = false, length = 500)
	private String definition; // Tradução ou significado

	@Column(length = 500)
	private String example; // Exemplo de uso (opcional)

	@ManyToOne
	@JoinColumn(name = "deck_id", nullable = false)
	private Deck deck;

	public FlashCard(String term, String definition, String example, Deck deck) {
		super();
		this.term = term;
		this.definition = definition;
		this.example = example;
		this.deck = new Deck(deck.getId());
	}

	public void atualizar(UpdateFlashCardDTO flashCardDTO) {
		if(flashCardDTO.example()!=null) 
			this.example = flashCardDTO.example();
		if(flashCardDTO.definition()!=null) 
			this.example = flashCardDTO.definition();
		if(flashCardDTO.term()!=null) 
			this.example = flashCardDTO.term();
	}
}
