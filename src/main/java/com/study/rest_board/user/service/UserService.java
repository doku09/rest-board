package com.study.rest_board.user.service;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.exception.UsernameAlreadyExistsException;
import com.study.rest_board.user.repository.UserRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepositoryJPA userRepository;

	/**
	 * 사용자 회원가입
	 * @param userDto
	 */
	@Transactional
	public void join(UserJoinRequestDto userDto) {

		if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new UsernameAlreadyExistsException("이미 존재하는 사용자입니다.");
		}

		if(null == userDto.getRole()){
			userDto.grantRole(UserRole.ROLE_USER);
		}

		userDto.changePassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		User user = userDto.toEntity();

		userRepository.save(user);
	}
}
