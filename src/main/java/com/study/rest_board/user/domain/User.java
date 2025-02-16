package com.study.rest_board.user.domain;

import com.study.rest_board.common.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role; //USER,ADMIN
}
