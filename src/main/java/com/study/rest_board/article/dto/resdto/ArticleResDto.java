package com.study.rest_board.article.dto.resdto;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.user.dto.response.UserResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleResDto {
	private long id;
	private String subject;
	private String content;
	private String regDt;
	private WriterResDto writer;
	private List<ArticleCommentResDto> comments;

	public static ArticleResDto from(Article article) {

		return ArticleResDto.builder()
			.id(article.getId())
			.subject(article.getSubject())
			.comments(article.getComments().stream().map(ArticleCommentResDto::from)
				.toList()
			)
			.writer(WriterResDto.builder().id(article.getWriter().getId()).name(article.getWriter().getUsername()).build())
			.content(article.getContent())
			.regDt(article.getRegDt().toString())
			.build();
	}

	public static ArticleResDto of(long id, String subject, String content,WriterResDto writer) {
		return ArticleResDto.builder()
			.id(id)
			.subject(subject)
			.content(content)
			.writer(writer)
			.build();
	}
}
