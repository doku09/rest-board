package com.study.rest_board.service;

import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.resdto.ArticleSaveReqDto;
import com.study.rest_board.entity.Article;
import com.study.rest_board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {

	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardRepository boardRepository;

	@Test
	@DisplayName("게시글 작성")
	void writeTest() {

		//given
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("테스트 제목")
			.content("내용입니다")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		//when
		ArticleResDto articleResDto = boardService.saveArticle(reqDto);

		//then
		Article article = boardRepository.findById(articleResDto.getId()).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
		Assertions.assertThat(articleResDto.getId()).isEqualTo(article.getId());
		Assertions.assertThat("테스트 제목").isEqualTo(article.getSubject());
		Assertions.assertThat("내용입니다").isEqualTo(article.getContent());
		Assertions.assertThat("abc1234").isEqualTo(article.getPassword());
	}
}