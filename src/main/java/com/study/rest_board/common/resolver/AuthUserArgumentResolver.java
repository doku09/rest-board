package com.study.rest_board.common.resolver;

import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.common.jwt.auth.AuthUserDto;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.user.exception.UserErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");

		boolean hasAuthUserAnnotation = parameter.hasParameterAnnotation(AuthUserInfo.class);

		// 예외처리
		boolean hasAuthUserType = AuthUserDto.class.isAssignableFrom(parameter.getParameterType());

		return hasAuthUserAnnotation && hasAuthUserType;
	 }

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		log.info("resolveArgument 실행");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			throw new GlobalBusinessException(UserErrorCode.USER_NOT_FOUND);
		}

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		return AuthUserDto.from(principalDetails.getUser());
	}
}
