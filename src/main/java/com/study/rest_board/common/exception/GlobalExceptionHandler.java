package com.study.rest_board.common.exception;

import com.study.rest_board.article.exception.ArticleErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public GlobalBusinessException argumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();

		List<String> errorMessages = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		String errorMessage = String.join("| ", errorMessages);

		return new GlobalBusinessException(errorMessage);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> globalException(GlobalBusinessException e) {
		return new ResponseEntity<>(new ErrorResult(e.getMessage(),e.getHttpStatus()),e.getHttpStatus());
	}

}
