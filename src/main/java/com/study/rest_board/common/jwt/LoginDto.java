package com.study.rest_board.common.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

	private String username;
	private String password;
}
