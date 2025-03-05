package com.study.rest_board.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.common.jwt.refresh.RefreshToken;
import com.study.rest_board.common.jwt.refresh.RefreshTokenRepository;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.response.UserResDto;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	ObjectMapper objectMapper = new ObjectMapper();

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshRepository;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		try {
			LoginDto user = objectMapper.readValue(request.getInputStream(), LoginDto.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

			return authenticationManager.authenticate(authenticationToken);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

		String username = principalDetails.getUser().getUsername();
		String role = principalDetails.getUser().getRole().name();

		String accessToken = jwtUtil.createJwt("access", username, role, 1);
		String refreshToken = jwtUtil.createJwt("refresh", username, role, 12*60);

		String userDto = objectMapper.writeValueAsString(UserResDto.from(principalDetails.getUser()));

		addRefresh(username,refreshToken, 12 * 60);


		response.getWriter().write(userDto);
		response.setHeader("access", accessToken);
		response.addCookie(createCookie("refresh", refreshToken));
		response.setStatus(HttpStatus.OK.value());
	}

	protected void addRefresh(String username, String refresh, int expiredMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, expiredMinute);
		Date date = calendar.getTime();

		RefreshToken refreshEntity = new RefreshToken();
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
