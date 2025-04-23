package com.renato.flashcards.flashcards_api.domain;

import java.util.ArrayList;
import java.util.List;

import com.renato.flashcards.flashcards_api.security.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "decks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Deck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@OneToMany(mappedBy = "deck", fetch = FetchType.LAZY)
	@Builder.Default
	private List<FlashCard> flashCards = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User owner;
	
	@OneToMany(mappedBy = "deck")
	private List<UserDeck> usersDecks;
	
	public Deck(String name) {
		super();
		this.name = name;
	}

	public Deck(Long id) {
		this.id = id;
	}

}
