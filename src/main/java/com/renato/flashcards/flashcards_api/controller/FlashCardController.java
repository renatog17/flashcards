package com.renato.flashcards.flashcards_api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.flashcards.flashcards_api.controller.dto.CreateFlashCardDTO;
import com.renato.flashcards.flashcards_api.controller.dto.UpdateFlashCardDTO;
import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.security.domain.User;
import com.renato.flashcards.flashcards_api.service.FlashCardService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/flashcard")
public class FlashCardController {

	private FlashCardService flashCardService;

	public FlashCardController(FlashCardService flashCardService) {
		super();
		this.flashCardService = flashCardService;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createFlashCard(@RequestBody @Valid CreateFlashCardDTO dto, UriComponentsBuilder uri,
			@AuthenticationPrincipal User user){
		FlashCard model = flashCardService.createFlashCard(dto);
		URI location = uri.path("api/flashcard/{id}").buildAndExpand(model.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> updateFlashCard(@PathVariable Long id, @RequestBody UpdateFlashCardDTO updateFlashCardDTO){
		return ResponseEntity.ok(flashCardService.updateFlashCard(updateFlashCardDTO, id));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteFlashCard(@PathVariable Long id){
		flashCardService.deleteFlashCard(id);
	    return ResponseEntity.noContent().build();
	}
}
