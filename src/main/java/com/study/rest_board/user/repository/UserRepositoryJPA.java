package com.study.rest_board.user.repository;

import com.study.rest_board.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);
}
