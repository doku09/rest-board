package com.study.rest_board.common.jwt;

import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.common.jwt.auth.CustomUserDetails;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.exception.UserErrorCode;
import com.study.rest_board.user.repository.UserRepository;
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
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// get token
		String authorization = request.getHeader(JwtProperties.HEADER_STRING);

		// 토큰 존재 여부 검증
		if (authorization == null || !authorization.startsWith(JwtProperties.TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.split(" ")[1];

		if (jwtUtil.isExpired(token)) {

			System.out.println("token expired");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//토큰에서 username과 role 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		//user를 생성하여 값 set
		User findUser = userRepository.findByUsername(username).orElseThrow(() -> new GlobalBusinessException(UserErrorCode.USER_NOT_FOUND));

		//UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(findUser);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
		//세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}