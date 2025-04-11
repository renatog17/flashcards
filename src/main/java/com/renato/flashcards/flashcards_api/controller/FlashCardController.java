package com.renato.flashcards.flashcards_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.flashcards.flashcards_api.controller.dto.CreateFlashCardDTO;
import com.renato.flashcards.flashcards_api.controller.dto.ReadFlashCardDTO;
import com.renato.flashcards.flashcards_api.controller.dto.UpdateFlashCardDTO;
import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.repository.FlashCardRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/flashcard")
public class FlashCardController {

	private FlashCardRepository flashCardRepository;

	public FlashCardController(FlashCardRepository flashCardRepository) {
		super();
		this.flashCardRepository = flashCardRepository;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createFlashCard(@RequestBody CreateFlashCardDTO dto, UriComponentsBuilder uri){
		FlashCard model = dto.toModel();
		flashCardRepository.save(model);
		URI location = uri.path("api/flashcard/{id}").buildAndExpand(model.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> readFlashCard(@PathVariable Long id){
		FlashCard flashCard = flashCardRepository.findById(id).orElseThrow();
		return ResponseEntity.ok(new ReadFlashCardDTO(flashCard));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateFlashCard(@PathVariable Long id, @RequestBody UpdateFlashCardDTO updateFlashCardDTO){
		FlashCard flashCard = flashCardRepository.findById(id).orElseThrow();
		flashCard.atualizar(updateFlashCardDTO);
		return ResponseEntity.ok(new ReadFlashCardDTO(flashCard));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteFlashCard(@PathVariable Long id){
		System.out.println("aqui");
		if (!flashCardRepository.existsById(id)) {
	        return ResponseEntity.notFound().build();
	    }

	    flashCardRepository.deleteById(id);
	    return ResponseEntity.noContent().build();
	}
}
