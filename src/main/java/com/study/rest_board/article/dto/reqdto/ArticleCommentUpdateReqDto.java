package com.study.rest_board.article.dto.reqdto;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ArticleCommentUpdateReqDto {

	private String content;
	@Setter
	private long userId;
	private long articleId;

	public ArticleComment toEntity(Article article, User user) {
		return ArticleComment.builder()
			.content(this.content)
			.article(article)
			.user(user)
			.build();

	}
}
