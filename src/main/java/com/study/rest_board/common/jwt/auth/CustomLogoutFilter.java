package com.study.rest_board.common.jwt.auth;

import com.study.rest_board.common.jwt.JWTUtil;
import com.study.rest_board.common.jwt.refresh.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * spring security의 LogoutFilter를 커스텀
 */
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		//path and method verify
		String requestUri = request.getRequestURI();
		if (!requestUri.matches("^\\/logout$")) {

			filterChain.doFilter(request, response);
			return;
		}
		String requestMethod = request.getMethod();
		if (!requestMethod.equals("POST")) {

			filterChain.doFilter(request, response);
			return;
		}

		//get refresh token
		String refresh = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("refresh")) {

				refresh = cookie.getValue();
			}
		}

		//refresh null check
		if (refresh == null) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		//expired check
		try {
			jwtUtil.isExpired(refresh);
		} catch (ExpiredJwtException e) {

			//response status code or 이미 로그아웃이 되어있다는 메시지
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
		String category = jwtUtil.getCategory(refresh);
		if (!category.equals("refresh")) {

			//response status code
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		//DB에 저장되어 있는지 확인
		Boolean isExist = refreshRepository.existsByRefresh(refresh);
		if (!isExist) {

			//response status code
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		//로그아웃 진행
		//Refresh 토큰 DB에서 제거
		refreshRepository.deleteByRefresh(refresh);

		//Refresh 토큰 Cookie 값 0
		Cookie cookie = new Cookie("refresh", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");

		response.addCookie(cookie);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
