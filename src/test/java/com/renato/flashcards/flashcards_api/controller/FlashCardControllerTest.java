package com.renato.flashcards.flashcards_api.controller;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.hibernate.mapping.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renato.flashcards.flashcards_api.controller.dto.CreateFlashCardDTO;
import com.renato.flashcards.flashcards_api.domain.Deck;
import com.renato.flashcards.flashcards_api.domain.FlashCard;
import com.renato.flashcards.flashcards_api.repository.DeckRepository;
import com.renato.flashcards.flashcards_api.repository.FlashCardRepository;
import com.renato.flashcards.flashcards_api.security.domain.User;
import com.renato.flashcards.flashcards_api.security.domain.UserRole;
import com.renato.flashcards.flashcards_api.security.repository.UserRepository;
import com.renato.flashcards.flashcards_api.security.service.TokenService;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FlashCardControllerTest {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DeckRepository deckRepository;
	@Autowired
	private FlashCardRepository flashCardRepository;

	@Transactional
	@Test
	public void testCreateFlashcard() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		Deck deck = new Deck("teste create flashcards");
		deck.setOwner(user);
		deckRepository.save(deck);
		CreateFlashCardDTO flashCardDTO = new CreateFlashCardDTO(deck.getId(), "Abacaxi", "ananas", "example teste");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/flashcard").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(flashCardDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/flashcard/1"));
	}

	@Transactional
	@Test
	public void deveImpedirUsuarioDeCriarFlashcardEmDeckDeOutroUsuario() throws JsonProcessingException, Exception {
		// arrange
		User user1 = new User("UserTest1", "UserTest1", UserRole.USER);
		userRepository.save(user1);
		Deck deck = new Deck("Deck do user 1");
		deck.setOwner(user1);
		deckRepository.save(deck);
		User user2 = new User("UserTest2", "UserTest2", UserRole.USER);
		userRepository.save(user2);
		String token = tokenService.generateToken(user2);

		CreateFlashCardDTO flashCardDTO = new CreateFlashCardDTO(deck.getId(), "Abacaxi", "ananas", "example teste");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/flashcard").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(flashCardDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Transactional
	@Test
	public void testCreateFlashcardDuplicadosEmUmMesmoDeck() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		Deck deck = new Deck("teste create flashcards");
		deck.setOwner(user);
		deckRepository.save(deck);
		FlashCard flashCard = new FlashCard("Term", "Definition", "Example", deck);
		flashCardRepository.save(flashCard);
		CreateFlashCardDTO flashCardDTO = new CreateFlashCardDTO(deck.getId(), "Term", "Definition", "Example");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/flashcard").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(flashCardDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Transactional
	@Test
	public void testCreateFlashcardDuplicadosEmUmDeckDiferente() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);

		Deck deck = new Deck("teste create flashcards");
		deck.setOwner(user);
		deckRepository.save(deck);

		Deck deck2 = new Deck("teste create flashcards 2");
		deck2.setOwner(user);
		deckRepository.save(deck2);

		FlashCard flashCard = new FlashCard("Term", "Definition", "Example", deck);
		flashCardRepository.save(flashCard);
		CreateFlashCardDTO flashCardDTO = new CreateFlashCardDTO(deck2.getId(), "Term", "Definition", "Example");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/flashcard").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(flashCardDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/flashcard/2"));
	}

	@Transactional
	@Test
	public void testCreateFlashcard_AllFieldsInvalid_ShouldReturnAllValidationErrors() throws Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);

		// Creating a DTO with all fields invalid (null idDeck, blank fields)
		CreateFlashCardDTO flashCardDTO = new CreateFlashCardDTO(null, // idDeck null
				" ", // term blank
				" ", // definition blank
				" " // example blank
		);

		// act & assert
		mockMvc.perform(MockMvcRequestBuilders.post("/flashcard").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(flashCardDTO)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.length()").value(4))
				.andExpect(jsonPath("$[?(@.campo == 'idDeck')].mensagem").value("não deve ser nulo"))
				.andExpect(jsonPath("$[?(@.campo == 'term')].mensagem").value("não deve estar em branco"))
				.andExpect(jsonPath("$[?(@.campo == 'definition')].mensagem").value("não deve estar em branco"))
				.andExpect(jsonPath("$[?(@.campo == 'example')].mensagem").value("não deve estar em branco"));
	}
	
	@Test
	@Transactional
	public void testDeleteFlashCardOfOtherUser() throws Exception {
		//arrange
		Deck deck = new Deck("deck");
		deckRepository.save(deck);
		
		FlashCard flashCard = new FlashCard("term", "definition", "example", deck );
		flashCardRepository.save(flashCard);
		
		ArrayList<FlashCard> flashCards = new ArrayList<FlashCard>();
		flashCards.add(flashCard);
		deck.setFlashCards(flashCards);
		
		User user = new User("teste", "teste", UserRole.USER);
		userRepository.save(user);
		deck.setOwner(user);
		
		User user2 = new User("teste1", "teste", UserRole.USER);
		String token = tokenService.generateToken(user2);
		userRepository.save(user2);
		
		//act
		mockMvc.perform(MockMvcRequestBuilders.delete("/flashcard/"+flashCard.getId())
				.header("Authorization", "Bearer " + token))
//				//assert
				.andExpect(status().isForbidden());
	}
	
	// cenários:
	// cadastrar flashcard sem token
	//
	
//	@Test
//	@Transactional
//	public void testReadHappyPath() throws Exception {
//		//arrange
//		Deck deck = new Deck("deck");
//		deckRepository.save(deck);
//		
//		FlashCard flashCard = new FlashCard("term", "definition", "example", deck );
//		flashCardRepository.save(flashCard);
//		
//		ArrayList<FlashCard> flashCards = new ArrayList<FlashCard>();
//		flashCards.add(flashCard);
//		deck.setFlashCards(flashCards);
//		
//		User user = new User("teste", "teste", UserRole.USER);
//		userRepository.save(user);
//		deck.setOwner(user);
//		
//		String token = tokenService.generateToken(user);
//		//act
//		mockMvc.perform(MockMvcRequestBuilders.get("/flashcard"+flashCard.getId())
//		.header("Authorization", "Bearer " + token))
//		//assert
//		.andExpect(status().isOk());
//		//agora colocar o teste do retorno json
//	}
//	
//	@Test
//	@Transactional
//	public void testReadFlashCardOfOtherUser(){
//		//arrange
//		//act
//		//assert
//	}
	
	//cenários read:
	//read happy path
	//read flashcard de outro user
}
