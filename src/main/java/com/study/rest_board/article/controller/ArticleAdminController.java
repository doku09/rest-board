package com.study.rest_board.article.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleAdminController {

	@GetMapping("/admin")
	public String test() {
		return "admin";
	}
	@GetMapping("/user")
	public String user() {
		return "user";
	}
}
