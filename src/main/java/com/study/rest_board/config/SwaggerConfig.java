package com.study.rest_board.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi boardGroupedOpenAi() {
		return GroupedOpenApi
			.builder()
			.group("board")
			.pathsToMatch("/board/**")
			.addOpenApiCustomizer(
				openApi ->
					openApi.setInfo(new Info()
						.title("board api")
						.description("게시판 API")
						.version("1.0.0")
					)
			)
			.build();
	}
}
