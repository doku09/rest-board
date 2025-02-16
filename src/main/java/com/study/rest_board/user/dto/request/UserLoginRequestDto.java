package com.study.rest_board.user.dto.request;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {
	private String username;
	private String password;


}
