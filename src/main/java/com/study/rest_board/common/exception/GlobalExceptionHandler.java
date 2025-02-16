package com.study.rest_board.common.exception;

import com.study.rest_board.article.exception.ErrorResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

/*	@ExceptionHandler
	public ResponseEntity<ErrorResult> globalException(RuntimeException e) {
		return new ResponseEntity<>(new ErrorResult("BAD",e.getMessage()), HttpStatus.BAD_REQUEST);
	}*/

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult globalException(RuntimeException e) {
		return new ErrorResult("BAD",e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResult argumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();

		List<String> errorMessages = bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		String errorMessage = String.join("| ", errorMessages);

		return new ErrorResult("BAD REQUEST",errorMessage);
	}


}
