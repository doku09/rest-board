package com.study.rest_board.article.exception;

public class ArticleNotFoundException extends RuntimeException{
	public ArticleNotFoundException() {
		super();
	}

	public ArticleNotFoundException(String message) {
		super(message);
	}

	public ArticleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArticleNotFoundException(Throwable cause) {
		super(cause);
	}

	protected ArticleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
