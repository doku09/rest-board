package com.study.rest_board.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.rest_board.user.domain.User;

import java.util.Date;

public class JwtUtil {
	private final String SECRET_KEY = "my-secret-key"; // 예제용 키

	public String generateToken(User user) {
		return JWT.create()
			.withSubject(user.getUsername())
			.withClaim("id", user.getId())
			.withClaim("role", user.getRole().name())
			.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 만료
			.sign(Algorithm.HMAC512(SECRET_KEY));
	}
}
