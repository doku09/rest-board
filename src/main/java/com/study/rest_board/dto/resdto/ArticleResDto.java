package com.study.rest_board.dto.resdto;

import com.study.rest_board.entity.Article;
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

	public static ArticleResDto from(Article article) {
		return ArticleResDto.builder()
			.id(article.getId())
			.subject(article.getSubject())
			.content(article.getContent())
			.regDt(article.getRegDt())
			.writerName(article.getWriterName())
			.password(article.getPassword())
			.build();
	}
}
