package com.study.rest_board.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.rest_board.common.jwt.auth.CustomUserDetails;
import com.study.rest_board.common.jwt.refresh.RefreshToken;
import com.study.rest_board.common.jwt.refresh.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
//	private final RefreshTokenRepository refreshRepository;
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		//클라이언트 요청에서 username, password 추출
//		String username = obtainUsername(request);
//		String password = obtainPassword(request);

		try {
			LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

			//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);

			//token에 담은 검증을 위한 AuthenticationManager로 전달
			return authenticationManager.authenticate(authToken);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String role = auth.getAuthority();

		String token = jwtUtil.createJwt(username, role, JwtProperties.EXPIRATION_TIME);

		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

//	protected void addRefresh(String username, String refresh, int expiredMinute) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MINUTE, expiredMinute);
//		Date date = calendar.getTime();
//
//		RefreshToken refreshEntity = new RefreshToken();
//		refreshEntity.setUsername(username);
//		refreshEntity.setRefresh(refresh);
//		refreshEntity.setExpiration(date.toString());
//
//		refreshRepository.save(refreshEntity);
//	}

	Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(12 * 60 * 60); // 12h
		cookie.setHttpOnly(true);   //JS로 접근 불가, 탈취 위험 감소
		return cookie;
	}
}
