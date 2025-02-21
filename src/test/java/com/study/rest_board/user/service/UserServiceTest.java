package com.study.rest_board.user.service;

import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder; // Mock으로 대체
@Test
@DisplayName("회원가입을 정상적으로 진행한다")
void 정상_회원가입() {

  //given
	UserJoinRequestDto requestDto = new UserJoinRequestDto("dong", "abcd1234!", UserRole.ROLE_USER);

	when(userRepository.findByUsername("dong")).thenReturn(Optional.empty());

	when(bCryptPasswordEncoder.encode("abcd1234!")).thenReturn("encodedPassword");
	//when
	userService.join(requestDto);
  //then
	verify(userRepository, times(1)).save(any(User.class)); // User 저장 확인
	assertEquals("encodedPassword", requestDto.getPassword()); // 비밀번호가 정상적으로 암호화되었는지 확인
}

}