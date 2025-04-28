package com.renato.flashcards.flashcards_api.controller;

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
import com.renato.flashcards.flashcards_api.repository.DeckRepository;
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
	// cenários:
	// teste criação token expirado
	// teste criação com nome nulo
}
