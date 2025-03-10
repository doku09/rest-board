package com.study.rest_board.common.jwt.auth;

import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.exception.UserErrorCode;
import com.study.rest_board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new GlobalBusinessException(UserErrorCode.USER_NOT_FOUND));

		return new CustomUserDetails(user);
	}
}
