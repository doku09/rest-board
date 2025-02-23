package com.study.rest_board.article.dto.resdto;

import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.user.dto.response.UserResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ArticleCommentResDto {
	private String content;
	private ArticleResDto article;
	private UserResDto user;

	public static ArticleCommentResDto from(ArticleComment entity) {
		ArticleResDto articleResDto = null;
		UserResDto userDto = null;

		if(null != entity.getArticle()) {
			articleResDto = ArticleResDto.from(entity.getArticle());
		}

		if(null != entity.getUser()) {
			userDto = UserResDto.from(entity.getUser());
		}
		return ArticleCommentResDto.builder()
			.content(entity.getContent())
			.article(articleResDto)
			.user(userDto)
			.build();
	}
}
