package com.study.rest_board.user.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
	public UsernameAlreadyExistsException(String message) {
		super(message);
	}
}
