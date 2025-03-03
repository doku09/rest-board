package com.study.rest_board.common.jwt.auth;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthUserDto {

	private long id;
	private String username;
	private UserRole role;

	@Builder
	public AuthUserDto(long id, String username,UserRole role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}

	public static AuthUserDto from(User user) {
		return AuthUserDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.role(user.getRole())
			.build();
	}
}
