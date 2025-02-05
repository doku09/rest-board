package com.study.rest_board.dto.resdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class ArticleResDto {
	private long id;
	private String subject;
	private String content;
	private String writerName;
	private LocalDateTime regDt;
	private String password;
}
