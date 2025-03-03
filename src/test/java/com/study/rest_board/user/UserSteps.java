package com.study.rest_board.user;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.common.jwt.auth.AuthUserDto;

public class UserSteps {

	public static AuthUserDto 관리자_권한_유저() {
		return AuthUserDto.builder().id(999L).role(UserRole.ROLE_ADMIN).build();
	}
	public static AuthUserDto 일반권한유저_생성(Long id) {
		return AuthUserDto.builder().id(id).role(UserRole.ROLE_USER).build();
	}

}
