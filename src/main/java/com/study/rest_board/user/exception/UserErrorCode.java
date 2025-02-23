package com.study.rest_board.user.exception;

import com.study.rest_board.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_ALREADY_EXIST("이미 존재하는 사용자입니다.",HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND("존재하는 사용자가 없습니다.",HttpStatus.BAD_REQUEST);
	private final String message;
	private final HttpStatus httpStatus;

}
