package com.study.rest_board.article.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResult {
	private String code;
	private String message;
}
