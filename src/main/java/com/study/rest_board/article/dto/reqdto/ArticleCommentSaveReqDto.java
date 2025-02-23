package com.study.rest_board.article.dto.reqdto;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class ArticleCommentSaveReqDto {
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
