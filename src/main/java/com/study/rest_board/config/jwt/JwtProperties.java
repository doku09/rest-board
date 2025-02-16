package com.study.rest_board.config.jwt;

public interface JwtProperties {
	String SECRET = "study";
	int EXPIRATION_TIME = 60000*10;
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
}
