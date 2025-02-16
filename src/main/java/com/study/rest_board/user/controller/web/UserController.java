package com.study.rest_board.user.controller.web;

import com.study.rest_board.user.service.UserService;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/auth")
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public String join(@RequestBody @Valid UserJoinRequestDto userDto){
		// 유효성 검사
		userService.join(userDto);
		return "complete!!";
	}
}
