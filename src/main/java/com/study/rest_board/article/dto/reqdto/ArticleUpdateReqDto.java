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
	private String writerName;
	private LocalDateTime regDt;
	private String password;

	public static ArticleUpdateReqDto of(String subject, String content, String password) {
		return ArticleUpdateReqDto.builder()
			.subject(subject)
			.content(content)
			.password(password)
			.build();
	}

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
