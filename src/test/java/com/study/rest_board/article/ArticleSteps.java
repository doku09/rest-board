package com.study.rest_board.article;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.article.dto.reqdto.ArticleUpdateReqDto;
import com.study.rest_board.user.domain.User;

import java.time.LocalDateTime;
import java.time.Month;

public class ArticleSteps {
	private LocalDateTime fixedDate = LocalDateTime.of(2025, Month.JANUARY,5,16,52);

	public static ArticleUpdateReqDto 수정게시글_요청_DTO() {
		ArticleUpdateReqDto reqDto = ArticleUpdateReqDto.builder()
			.subject("수정입니다.")
			.content("수정 내용 테스트")
			.build();
		return reqDto;
	}

	public static Article 게시글_엔터티_생성() {
		Article article = new Article(1L, "subject", "content", LocalDateTime.of(2025, Month.JANUARY,5,16,52));
		article.setWriter(User.builder().id(1L).build());
		return article;
	}

	public static ArticleComment 댓글_엔터티_생성(Long id) {
		return ArticleComment.builder()
			.id(id)
			.user(User.builder().id(1L).build())
			.build();
	}
}
