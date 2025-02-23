package com.study.rest_board.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResult {

	private String message;
	private HttpStatus httpStatus;

	public ErrorResult(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
