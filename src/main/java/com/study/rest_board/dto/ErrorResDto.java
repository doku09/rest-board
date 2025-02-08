package com.study.rest_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
//@Data
@Getter
public class ErrorResDto {
	private String code;
	private String message;
}
