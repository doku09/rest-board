package com.study.rest_board.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.exception.UserErrorCode;
import com.study.rest_board.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

		if(null == jwtHeader || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
			chain.doFilter(request,response);
			return;
		}

		String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();


		// 서명이 정상적으로 됨
		if(null != username) {
			User user = userRepository.findByUsername(username).orElseThrow(() -> new GlobalBusinessException(UserErrorCode.USER_NOT_FOUND));
			PrincipalDetails principalDetails = new PrincipalDetails(user);

			// 강제로 인증 객체 생성
			Authentication authentication  = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request,response);
		}
	}
}
