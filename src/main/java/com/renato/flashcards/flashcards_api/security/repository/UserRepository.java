package com.renato.flashcards.flashcards_api.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.renato.flashcards.flashcards_api.security.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

	UserDetails findByLogin(String login);
}