package com.study.rest_board.common.exception;

public class GlobalRuntimeException extends RuntimeException{
	public GlobalRuntimeException() {
		super();
	}

	public GlobalRuntimeException(String message) {
		super(message);
	}

	public GlobalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GlobalRuntimeException(Throwable cause) {
		super(cause);
	}

	protected GlobalRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
