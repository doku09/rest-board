package com.study.rest_board.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// get token
		String token = request.getHeader(JwtProperties.HEADER_STRING);

		// 토큰 존재 여부 검증
		if (token == null || !token.startsWith("Bearer ")){
			filterChain.doFilter(request, response);
			return;
		}

		token = token.split(" ")[1];

		try{
		// 토큰 만료 검증
		if (jwtUtil.isExpired(token)){
			filterChain.doFilter(request, response);
		} } catch (ExpiredJwtException e) {
			response.getWriter().write("access token is expired");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}


		String category = jwtUtil.getCategory(token);

		// 카테고리 검사(access)
		if (!category.equals("access")){
			response.getWriter().write("invalid access token");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		// 임시 세션 추가
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		PrincipalDetails principalDetails = new PrincipalDetails();
		principalDetails.getUser().setRole(role);
		principalDetails.getUser().setUsername(username);

		// 세션 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(username, null, principalDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		System.out.println("Success");
		filterChain.doFilter(request, response);
	}
}
