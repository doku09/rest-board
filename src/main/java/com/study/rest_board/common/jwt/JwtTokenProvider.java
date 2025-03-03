package com.study.rest_board.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;

import java.util.Date;

public class JwtTokenProvider {

	protected String generateToken(PrincipalDetails userDetails) {
		return JWT.create()
			.withSubject(userDetails.getUsername())
			.withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
			.withClaim("username", userDetails.getUser().getUsername())
			.sign(Algorithm.HMAC512(JwtProperties.SECRET));

	}
}
