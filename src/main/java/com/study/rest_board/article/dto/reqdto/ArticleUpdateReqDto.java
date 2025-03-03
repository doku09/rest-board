package com.study.rest_board.article.dto.reqdto;

import com.study.rest_board.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArticleUpdateReqDto {
	private long id;
	private String subject;
	private String content;
	private LocalDateTime regDt;

	public static ArticleUpdateReqDto of(String subject, String content, String password) {
		return ArticleUpdateReqDto.builder()
			.subject(subject)
			.content(content)
			.build();
	}

	public Article toEntity() {
		return Article.builder()
			.subject(this.subject)
			.content(this.content)
			.regDt(LocalDateTime.now())
			.build();
	}
}
