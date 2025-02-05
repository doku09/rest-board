package com.study.rest_board.dto.resdto;

import com.study.rest_board.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class ArticleSaveReqDto {
	private long id;
	private String subject;
	private String content;
	private String writerName;
	private LocalDateTime regDt;
	private String password;


	public Article toEntity() {
		return Article.builder()
			.subject(this.subject)
			.content(this.content)
			.writerName(this.writerName)
			.password(this.password)
			.regDt(LocalDateTime.now())
			.build();
	}
}
