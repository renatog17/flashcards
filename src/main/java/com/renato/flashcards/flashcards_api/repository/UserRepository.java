package com.renato.flashcards.flashcards_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.flashcards.flashcards_api.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
