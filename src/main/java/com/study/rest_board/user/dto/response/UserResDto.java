package com.study.rest_board.user.dto.response;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class UserResDto {

	private long id;
	private String username;
	private UserRole role;

	public static UserResDto from(User user) {
		return UserResDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.role(user.getRole())
			.build();
	}
}
