package com.study.rest_board.article.exception.advice;

import com.study.rest_board.article.exception.ArticleNotFoundException;
import com.study.rest_board.article.exception.ErrorResult;
import com.study.rest_board.article.exception.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.study.rest_board.article.controller")
public class ControllerAdvice {


	@ExceptionHandler
	public ResponseEntity<ErrorResult> noMatchPassword(InvalidPasswordException e) {
		return new ResponseEntity<>(new ErrorResult("INVALID_PASSWORD",e.getMessage()),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> notFountArticle(ArticleNotFoundException e) {
		return new ResponseEntity<>(new ErrorResult("NOT_FOUND_ARTICLE",e.getMessage()),HttpStatus.NOT_FOUND);
	}

}
