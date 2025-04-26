package com.renato.flashcards.flashcards_api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renato.flashcards.flashcards_api.controller.dto.CreateDeckDTO;
import com.renato.flashcards.flashcards_api.security.domain.User;
import com.renato.flashcards.flashcards_api.security.domain.UserRole;
import com.renato.flashcards.flashcards_api.security.repository.UserRepository;
import com.renato.flashcards.flashcards_api.security.service.TokenService;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeckControllerTest {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Test
	public void testCreateDeck() throws JsonProcessingException, Exception {
		//arrange
		User user = new User("UserTest", "UserTest", UserRole.USER);
		userRepository.save(user);
		String token = tokenService.generateToken(user);
		
		
		CreateDeckDTO createDeckDTO = new CreateDeckDTO("Teste deck");
		//act && assert
		mockMvc.perform(MockMvcRequestBuilders.post("/deck").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(createDeckDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	//cenários:
	//teste criação happy path
	//teste criação com nome vazio e nulo
	//teste criação sem token
	//teste criação usuario deletado
	//teste criação nome duplicado
	//teste criação token expirado
	//teste limite de caracteres
	//teste locale
}
