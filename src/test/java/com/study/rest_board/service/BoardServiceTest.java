package com.study.rest_board.service;

import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.reqdto.ArticleSaveReqDto;
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
	void saveArticleTest() {

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
		Assertions.assertThat("테스트 제목1").isEqualTo(article.getSubject());
		Assertions.assertThat("내용입니다").isEqualTo(article.getContent());
		Assertions.assertThat("초롱이").isEqualTo(article.getWriterName());
		Assertions.assertThat("abc1234").isEqualTo(article.getPassword());
	}

	@Test
	@DisplayName("게시글 조회")
	void findArticleById() {

    //given
		ArticleResDto resDto = boardService.saveArticle(ArticleSaveReqDto.builder()
			.subject("조회 테스트")
			.content("조회 내용")
			.writerName("파랑이")
			.password("abc1234")
			.build());

		long id = resDto.getId();

		//when
		ArticleResDto viewResDto = boardService.findArticleById(id);

		//then
		Assertions.assertThat(id).isEqualTo(viewResDto.getId());
		Assertions.assertThat("조회 테스트").isEqualTo(viewResDto.getSubject());
		Assertions.assertThat("조회 내용").isEqualTo(viewResDto.getContent());
		Assertions.assertThat("파랑이").isEqualTo(viewResDto.getWriterName());
		Assertions.assertThat("abc1234").isEqualTo(viewResDto.getPassword());
	}
}