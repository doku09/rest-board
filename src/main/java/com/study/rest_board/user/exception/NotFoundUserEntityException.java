package com.study.rest_board.user.exception;

public class NotFoundUserEntityException extends RuntimeException {

	public NotFoundUserEntityException() {
		super("요청한 사용자가 없습니다.");
	}

	public NotFoundUserEntityException(String message) {
		super(message);
	}
}
