package com.study.rest_board.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {

	private final SecretKey secretKey;

	// 비밀키 값을 SecretKey 객체로 반환
	public JwtUtil(@Value("${spring.jwt.key}") String key) {
		this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
		System.out.println("secretKey = " + secretKey);
	}

	// 토큰 생성
	public String createJwt(String category,String username, String role, int expiredMinute){
		// iat, exp를 위한 Date 및 Calendar
		Calendar expCalendar = Calendar.getInstance();
		expCalendar.add(Calendar.MINUTE, Math.toIntExact(expiredMinute));
		Date iatDate = new Date();
		Date expDate = expCalendar.getTime();

		return Jwts.builder()
			.claim("category",category)
			.claim("username", username)
			.claim("role", role)
			.issuedAt(iatDate)
			.expiration(expDate)
			.signWith(secretKey)
			.compact();
	}

	// 토큰 검증 - 카테고리
	public String getCategory(String token){
		Jws<Claims> jws =  Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
		return jws.getPayload().get("category", String.class);
	}

	// 토큰 검증 - 아이디
	public String getUsername(String token){
		Jws<Claims> jws =  Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
		return jws.getPayload().get("username", String.class);
	}

	// 토큰 검증 - role
	public String getRole(String token){
		Jws<Claims> jws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
		return jws.getPayload().get("role", String.class);
	}

	// 토큰 검증 - 토큰 유효기간 비교
	public Boolean isExpired(String token){

		try{
			Jws<Claims> jws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			Date expDate = jws.getPayload().getExpiration();
			// 현재 날짜가 exp 날짜보다 뒤에 있으면, 만료됨
			return new Date().after(expDate);

		} catch (ExpiredJwtException e){
			e.printStackTrace();
			return true;
		}

	}

}
