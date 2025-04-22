package com.renato.flashcards.flashcards_api.security.controller.dto;

import com.renato.flashcards.flashcards_api.security.domain.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {

}