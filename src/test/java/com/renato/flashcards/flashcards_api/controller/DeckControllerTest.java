package com.renato.flashcards.flashcards_api.controller;

import java.util.ArrayList;

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
import com.renato.flashcards.flashcards_api.controller.dto.CreateDeckDTO;
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
public class DeckControllerTest {

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
	public void testCreateDeck() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("Teste deck");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(createDeckDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/deck/1"));

	}

	@Transactional
	@Test
	public void testCreatDeckWithEmptyName() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(createDeckDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("name"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("não deve estar em branco"));
	}

	@Transactional
	@Test
	public void testCreatDeckWithoutToken() throws JsonProcessingException, Exception {
		// arrange
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("");
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDeckDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Transactional
	@Test
	public void testCreatDeckWithNameDuplicate() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("Teste");
		Deck deck = new Deck("Teste");
		deckRepository.save(deck);
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(createDeckDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/deck/2"));
	}

	@Transactional
	@Test
	public void testCreatDeckWithNameBiggerThan50Carachteres() throws JsonProcessingException, Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("a".repeat(51));
		// act
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(createDeckDTO)))
				// assert
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].campo").value("name"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("deve ter no máximo 50 caracteres"));
	}

	// delete tests starts here
	@Test
	public void testDeleteWithoutToken() throws Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);

		Deck deck = new Deck("Teste");
		deckRepository.save(deck);

		deck.setOwner(user);
		// act
		mockMvc.perform(MockMvcRequestBuilders.delete("/deck/1"))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@Transactional
	public void testDeleteHappyPath() throws Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		Deck deck = new Deck("Teste");
		deck.setOwner(user);
		deckRepository.save(deck);

		String token = tokenService.generateToken(user);
		// act
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/deck/" + deck.getId()).header("Authorization", "Bearer " + token))
				// assert
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	@Transactional
	public void testDeleteWhenIdDontExist() throws Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		// act
		mockMvc.perform(MockMvcRequestBuilders.delete("/deck/1").header("Authorization", "Bearer " + token))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@Transactional
	public void testDeleteDeckWIthFlashCards() throws Exception {
		// arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		Deck deck = new Deck("Teste");
		deck.setOwner(user);
		deckRepository.save(deck);
		FlashCard flashCard = new FlashCard("term", "definition", "Example", deck);
		flashCardRepository.save(flashCard);
		String token = tokenService.generateToken(user);
		// act
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/deck/" + deck.getId()).header("Authorization", "Bearer " + token))
				// assert
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

	@Test
	@Transactional
	public void testDeleteDeckOfOtherUser() throws Exception {
		// arrange
		User user1 = new User("UserTest1", "UserTest1", UserRole.USER);
		userRepository.save(user1);
		User user2 = new User("UserTest2", "UserTest2", UserRole.USER);
		userRepository.save(user2);
		Deck deck = new Deck("Teste1");
		deck.setOwner(user1);
		deckRepository.save(deck);
		String tokenUser2 = tokenService.generateToken(user2);
		// act
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/deck/" + deck.getId()).header("Authorization", "Bearer " + tokenUser2))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// teste read
	// happypath get decks sem flashcards
	// para esse teste, eu crio instancias de decks de outros users, e teste se vai
	// retornar somente os dados de um
	// específico
	@Test
	@Transactional
	public void shouldReturnOnlyDecksOfAuthenticatedUser() throws Exception {
		// arrange
		User user1 = new User("user1", "teste", UserRole.USER);
		User user2 = new User("user2", "teste", UserRole.USER);
		User user3 = new User("user3", "teste", UserRole.USER);
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		Deck deck1 = new Deck("deck1");
		Deck deck2 = new Deck("deck2");
		Deck deck3 = new Deck("deck3");
		deck1.setOwner(user1);
		deck2.setOwner(user2);
		deck3.setOwner(user3);
		deckRepository.save(deck1);
		deckRepository.save(deck2);
		deckRepository.save(deck3);

		String tokenUser1 = tokenService.generateToken(user1);
		// act
		mockMvc.perform(MockMvcRequestBuilders.get("/deck").header("Authorization", "Bearer " + tokenUser1))
				// assert
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(deck1.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(deck1.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
	}

	@Test
	@Transactional
	public void accessDecksWithoutFlashcards_returns403WhenUserHasNoPermission() throws Exception {
		// arrange
		User user1 = new User("user1", "teste", UserRole.USER);
		userRepository.save(user1);
		Deck deck1 = new Deck("deck1");
		deck1.setOwner(user1);
		deckRepository.save(deck1);

		// act
		mockMvc.perform(MockMvcRequestBuilders.get("/deck"))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@Transactional
	public void shouldReturnOnlyDecksWithFlashCardsOfAuthenticatedUser() throws Exception {
		// arrange
		User user1 = new User("user1", "teste", UserRole.USER);
		User user2 = new User("user2", "teste", UserRole.USER);
		User user3 = new User("user3", "teste", UserRole.USER);
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		Deck deck1 = new Deck("deck1");
		Deck deck2 = new Deck("deck2");
		Deck deck3 = new Deck("deck3");
		deck1.setOwner(user1);
		deck2.setOwner(user2);
		deck3.setOwner(user3);
		deckRepository.save(deck1);
		deckRepository.save(deck2);
		deckRepository.save(deck3);

		FlashCard flashCard = new FlashCard("term", "definition", "example", deck1);
		FlashCard flashCard2 = new FlashCard("term2", "definition2", "example2", deck2);
		FlashCard flashCard3 = new FlashCard("term3", "definition3", "example3", deck3);
		flashCardRepository.save(flashCard);
		flashCardRepository.save(flashCard2);
		flashCardRepository.save(flashCard3);

		ArrayList<FlashCard> flashCards1 = new ArrayList<FlashCard>();
		flashCards1.add(flashCard);
		deck1.setFlashCards(flashCards1);
		ArrayList<FlashCard> flashCards2 = new ArrayList<FlashCard>();
		flashCards2.add(flashCard2);
		deck2.setFlashCards(flashCards2);

		String tokenUser1 = tokenService.generateToken(user1);
		// act
		mockMvc.perform(MockMvcRequestBuilders.get("/deck/flashcards").header("Authorization", "Bearer " + tokenUser1))
				// assert
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(deck1.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(deck1.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].flashCards.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].flashCards[0].id").value(flashCard.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].flashCards[0].term").value(flashCard.getTerm()));
	}

	@Test
	@Transactional
	public void accessDecksWithFlashcards_returns403WhenUserHasNoPermission() throws Exception {
		// arrange
		User user1 = new User("user1", "teste", UserRole.USER);
		userRepository.save(user1);
		Deck deck1 = new Deck("deck1");
		deck1.setOwner(user1);
		deckRepository.save(deck1);

		// act
		mockMvc.perform(MockMvcRequestBuilders.get("/deck/flashcards"))
				// assert
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
