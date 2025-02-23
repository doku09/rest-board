package com.study.rest_board.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String getMessage();
	HttpStatus getHttpStatus();
}
