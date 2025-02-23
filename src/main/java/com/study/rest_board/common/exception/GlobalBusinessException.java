package com.study.rest_board.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalBusinessException extends RuntimeException{

	private final String message;
	private final HttpStatus httpStatus;

	public GlobalBusinessException(String message) {
		this.message = message;
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	public GlobalBusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
