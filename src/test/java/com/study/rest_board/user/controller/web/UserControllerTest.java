package com.study.rest_board.user.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.JwtUtil;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.dto.request.UserLoginRequestDto;
import com.study.rest_board.user.repository.UserRepository;
import com.study.rest_board.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@WithMockUser(username = "test11", password = "abc1234", roles = "USER")
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private JwtUtil jwtUtil;


	@DisplayName("정상적으로 회원가입을 완료한다.")
	@ParameterizedTest()
	@MethodSource("usernameAndPassword")
	void 회원가입_완료(String username, String password) throws Exception {

		//given
		UserJoinRequestDto requestDto = new UserJoinRequestDto(username, password, UserRole.ROLE_USER);

		//when then
		mockMvc.perform(post("/auth/join")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(objectMapper.writeValueAsString(requestDto))
			)
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입을 테스트한다. - 아이디 유효성검사 오류")
	void 회원가입_아이디_유효성검사() throws Exception {

		//given
		UserJoinRequestDto requestDto = new UserJoinRequestDto("dfd", "abc1234!", UserRole.ROLE_USER);

		//when then
		mockMvc.perform(post("/auth/join")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(objectMapper.writeValueAsString(requestDto))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").exists())
			.andExpect(jsonPath("$.message").value("사용자 이름은 4 ~ 10자로 입력해야하며, 알파벳 소문자와 숫자만 포함해야 합니다."));
	}

	@Test
	@DisplayName("정상적인 로그인 요청 시 JWT를 발급한다.")
	@WithMockUser(username = "test11", roles = "USER")
	void 로그인_성공() throws Exception {
		System.out.println("UserControllerTest.로그인_성공");
		//given
		UserLoginRequestDto requestDto = new UserLoginRequestDto("test11", "abc1234!");

		String fakeJwt = "Bearer fake-jwt-token";
		when(jwtUtil.generateToken(any(User.class))).thenReturn(fakeJwt);

		//when then
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto))
				.with(csrf())
			)
			.andExpect(status().isOk()) // 200 상태코드 기대
			.andExpect(header().exists("Authorization")) // JWT 헤더 존재 여부 확인
			.andExpect(header().string("Authorization", fakeJwt)); // 반환된 JWT 확인
	}

	static Stream<Arguments> usernameAndPassword() {
		return Stream.of(
			Arguments.arguments("abc", "Aautorizen123!"),
			Arguments.arguments("abc!!", "abc1234"),
			Arguments.arguments("abc1234", "abc1234!")
		);
	}

}