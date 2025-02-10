package com.study.rest_board.service;

import com.study.rest_board.dto.reqdto.PasswordReqDto;
import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.entity.Article;
import com.study.rest_board.exception.ArticleNotFoundException;
import com.study.rest_board.exception.InvalidPasswordException;
import com.study.rest_board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class BoardServiceTest {

	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private DataSource dataSource;

	@BeforeEach
	void setup() {
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("test 제목")
			.content("내용입니다")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		boardService.saveArticle(reqDto);
	}

	@Test
	void testDataSource() throws SQLException {
		System.out.println(dataSource.getConnection().getMetaData().getURL());
	}

	@Test
	@DisplayName("게시글 작성")
	void saveArticleTest() {

		//given
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("작성 테스트")
			.content("내용입니다")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		//when
		ArticleResDto articleResDto = boardService.saveArticle(reqDto);

		//then
		Article article = boardRepository.findById(articleResDto.getId()).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
		assertThat(articleResDto.getId()).isEqualTo(article.getId());
		assertThat("작성 테스트").isEqualTo(article.getSubject());
		assertThat("내용입니다").isEqualTo(article.getContent());
		assertThat("초롱이").isEqualTo(article.getWriterName());
		assertThat("abc1234").isEqualTo(article.getPassword());
	}

	@Test
	@DisplayName("게시글 조회")
	void findArticleById() {

		//given
		//when
		ArticleResDto viewResDto = boardService.findArticleById(1);

		//then
		assertThat(1).isEqualTo(viewResDto.getId());
		assertThat("test 제목").isEqualTo(viewResDto.getSubject());
		assertThat("내용입니다").isEqualTo(viewResDto.getContent());
		assertThat("초롱이").isEqualTo(viewResDto.getWriterName());
		assertThat("abc1234").isEqualTo(viewResDto.getPassword());
	}

	@Test
	@DisplayName("게시글 조회 실패")
	void failFindArticleTest() {

		//given
		//when
		//then
		assertThatThrownBy(() -> boardService.findArticleById(2))
			.isInstanceOf(ArticleNotFoundException.class);
	}

	@Test
	@DisplayName("게시글 수정")
	void updateArticle() {

		//given
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("수정입니다.")
			.content("수정 내용 테스트")
			.writerName("초롱이")
			.password("abc1234")
			.build();
		//when
		ArticleResDto articleResDto;
		articleResDto = boardService.updateArticleById(1, reqDto);
		//then
		assertThat(articleResDto.getSubject()).isEqualTo("수정입니다.");
		assertThat(articleResDto.getContent()).isEqualTo("수정 내용 테스트");
		assertThat(articleResDto.getPassword()).isEqualTo("abc1234");
	}

	@Test
	@DisplayName("게시글 수정 - 패스워드 일치하지 않음")
	void no_match_password() {

		//given
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("수정입니다.")
			.content("수정 내용 테스트")
			.writerName("초롱이")
			.password("aaaa")
			.build();
	  //when
	  //then
		assertThatThrownBy(() -> boardService.updateArticleById(1, reqDto))
			.isInstanceOf(InvalidPasswordException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.")
		;
	}
	
	@Test
	@DisplayName("게시글 삭제")
	void deleteArticle() {
		
	  //given
		long id = 1;
		PasswordReqDto passwordReqDto = new PasswordReqDto("abc1234");

		//when
		boardService.deleteArticleById(1,passwordReqDto);
	
	  //then
		assertThat(boardRepository.findById(id)).isEmpty();
	}

}