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
	private Long articleId;
	private UserResDto user;

	public static ArticleCommentResDto from(ArticleComment entity) {
		UserResDto userDto = null;

		if(null != entity.getUser()) {
			userDto = UserResDto.from(entity.getUser());
		}

		return ArticleCommentResDto.builder()
			.content(entity.getContent())
			.articleId(entity.getArticle().getId())
			.user(userDto)
			.build();
	}
}
