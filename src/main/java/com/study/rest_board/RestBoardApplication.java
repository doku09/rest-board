package com.study.rest_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class RestBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestBoardApplication.class, args);
	}

}
