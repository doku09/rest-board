package com.study.rest_board.common.controller;

import com.study.rest_board.common.jwt.JWTUtil;
import com.study.rest_board.common.jwt.JwtProperties;
import com.study.rest_board.common.jwt.refresh.RefreshEntity;
import com.study.rest_board.common.jwt.refresh.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;

	// TODO 서비스단으로 로직 이동하기
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

		//get refresh token
		String refresh = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("refresh")) {

				refresh = cookie.getValue();
			}
		}

		if (refresh == null) {

			//response status code
			return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
		}

		//expired check
		try {
			jwtUtil.isExpired(refresh);
		} catch (ExpiredJwtException e) {

			//response status code
			return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
		}

		// 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtil.getCategory(refresh);

		if (!category.equals("refresh")) {

			//response status code
			return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
		}

		//DB에 저장되어 있는지 확인
		Boolean isExist = refreshRepository.existsByRefresh(refresh);
		if (!isExist) {

			//response body
			return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtil.getUsername(refresh);
		String role = jwtUtil.getRole(refresh);

		//make new JWT
		String newAccess = jwtUtil.createJwt("access", username, role, JwtProperties.ACCESS_TOKEN_TIME);
		String newRefresh = jwtUtil.createJwt("refresh", username, role, JwtProperties.REFRESH_TOKEN_TIME);

		//Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
		refreshRepository.deleteByRefresh(refresh);
		addRefresh(username, newRefresh);

		//response
		response.setHeader("access", newAccess);
		response.addCookie(createCookie("refresh", newRefresh));


		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void addRefresh(String username, String refresh) {

		Date date = new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_TIME);

		RefreshEntity refreshEntity = new RefreshEntity();
		refreshEntity.setUsername(username);
		refreshEntity.setRefresh(refresh);
		refreshEntity.setExpiration(date.toString());

		refreshRepository.save(refreshEntity);
	}

	Cookie createCookie(String key, String value){
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(12*60*60); // 12h
		cookie.setHttpOnly(true);   //JS로 접근 불가, 탈취 위험 감소
		return cookie;
	}
}
