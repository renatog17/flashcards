package com.renato.flashcards.flashcards_api.domain;
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
}

