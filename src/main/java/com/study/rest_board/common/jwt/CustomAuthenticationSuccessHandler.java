package com.study.rest_board.common.jwt;

import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		JwtTokenProvider jwtProvider = new JwtTokenProvider();
		String jwtToken = jwtProvider.generateToken(principalDetails);


		//유저정보 반환
		response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);


	}
}
