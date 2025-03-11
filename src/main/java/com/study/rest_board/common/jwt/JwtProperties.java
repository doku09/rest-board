package com.study.rest_board.common.jwt;

public interface JwtProperties {
	String SECRET = "study";
	int EXPIRATION_TIME = 60000*30;
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";

	Long ACCESS_TOKEN_TIME = 600000L;
	Long REFRESH_TOKEN_TIME = 86400000L;
}
