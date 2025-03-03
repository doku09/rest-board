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
public class ArticleSaveReqDto {
	private String subject;
	private String content;
	private LocalDateTime regDt;

	public static ArticleSaveReqDto of(String subject, String content) {
		return ArticleSaveReqDto.builder()
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
