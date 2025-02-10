package com.study.rest_board.exception.advice;

import com.study.rest_board.exception.ArticleNotFoundException;
import com.study.rest_board.exception.ErrorResult;
import com.study.rest_board.exception.InvalidPasswordException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.study.rest_board.controller")
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
