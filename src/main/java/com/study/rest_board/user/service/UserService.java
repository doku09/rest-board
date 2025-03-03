package com.study.rest_board.user.service;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.exception.UserErrorCode;
import com.study.rest_board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;

	/**
	 * 사용자 회원가입
	 * @param userDto
	 */
	@Transactional
	public void join(UserJoinRequestDto userDto) {

		if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new GlobalBusinessException(UserErrorCode.USER_ALREADY_EXIST);
		}

		if(null == userDto.getRole()){
			userDto.grantRole(UserRole.ROLE_USER);
		}

		userDto.changePassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		User user = userDto.toEntity();

		userRepository.save(user);
	}
}
