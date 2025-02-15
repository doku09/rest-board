package com.study.rest_board.article.dto.reqdto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordReqDto {

	private String password;


	public PasswordReqDto(String password) {
		this.password = password;
	}
}
