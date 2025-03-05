package com.study.rest_board.common.jwt.refresh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	private String username;
	private String refresh;
	private String expiration;
}
