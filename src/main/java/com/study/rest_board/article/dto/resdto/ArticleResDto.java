package com.study.rest_board.article.dto.resdto;

import com.study.rest_board.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleResDto {
	private long id;
	private String subject;
	private String content;
	private String writerName;
	private String regDt;
	private String password;

	public static ArticleResDto from(Article article) {
		return ArticleResDto.builder()
			.id(article.getId())
			.subject(article.getSubject())
			.content(article.getContent())
			.regDt(article.getRegDt().toString())
			.writerName(article.getWriterName())
			.password(article.getPassword())
			.build();
	}

	public static ArticleResDto of(long id, String subject, String content, String writerName) {
		return ArticleResDto.builder()
			.id(id)
			.subject(subject)
			.content(content)
			.writerName(writerName)
			.build();
	}
}
