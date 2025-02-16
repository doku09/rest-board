package com.study.rest_board.user.dto.request;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequestDto {

	@Pattern(regexp = "^[a-z0-9]{4,10}$", message = "사용자 이름은 4 ~ 10자로 입력해야하며, 알파벳 소문자와 숫자만 포함해야 합니다.")
	private String username;
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+{}|,.<>\\/?'`~]).{8,15}$",
		message = "비밀번호는 8자 이상 15자 이하, 대소문자, 숫자 및 특수문자가 포함되어야 합니다.")
	private String password;
	private UserRole role;

	public User toEntity() {
		return User.builder()
			.username(this.username)
			.password(this.password)
			.role(this.role)
			.build();
	}
	public void changePassword(String password) {
		this.password = password;
	}
	public void grantRole(UserRole role) {
		this.role = role;
	}
}
