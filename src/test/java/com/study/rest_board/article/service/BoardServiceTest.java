package com.study.rest_board.article.service;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.article.dto.reqdto.PasswordReqDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.exception.ArticleNotFoundException;
import com.study.rest_board.article.exception.InvalidPasswordException;
import com.study.rest_board.article.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class BoardServiceTest {

	@Mock
	BoardRepository boardRepository;
	@InjectMocks
	private BoardService boardService;


	@Test
	@DisplayName("게시글 작성")
	void saveArticleTest() {

		// given
		LocalDateTime fixedTime = LocalDateTime.of(2024, 2, 19, 12, 0, 0); // 고정된 시간 설정
		Article article = new Article(1, "작성 테스트", "내용입니다", "초롱이",fixedTime, "abc1234");
		when(boardRepository.save(any(Article.class))).thenReturn(article);

		// when
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("작성 테스트")
			.content("내용입니다")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		ArticleResDto articleResDto = boardService.saveArticle(reqDto);

		// then
		assertThat(articleResDto.getId()).isEqualTo(article.getId());
		assertThat("작성 테스트").isEqualTo(articleResDto.getSubject());
		assertThat("내용입니다").isEqualTo(articleResDto.getContent());
		assertThat("초롱이").isEqualTo(articleResDto.getWriterName());
		assertThat("abc1234").isEqualTo(articleResDto.getPassword());
	}

	@Test
	@DisplayName("선택한 게시글 조회에 성공한다.")
	void findArticleById() {

		// given
		Article article = new Article(1, "작성 테스트", "내용입니다", "초롱이", LocalDateTime.now(), "abc1234");

		// when
		when(boardRepository.findById(1L)).thenReturn(Optional.of(article));

		// then
		ArticleResDto viewResDto = boardService.findArticleById(1);

		assertThat(1).isEqualTo(viewResDto.getId());
		assertThat("작성 테스트").isEqualTo(viewResDto.getSubject());
		assertThat("내용입니다").isEqualTo(viewResDto.getContent());
		assertThat("초롱이").isEqualTo(viewResDto.getWriterName());
		assertThat("abc1234").isEqualTo(viewResDto.getPassword());
	}

	@Test
	@DisplayName("아이디로 게시글 선택조회시 아이디에 해당하는 게시글이 없으면 실패한다.")
	void failFindArticleTest() {

		//given
		when(boardRepository.findById(5L)).thenReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> boardService.findArticleById(5L))
			.isInstanceOf(ArticleNotFoundException.class);
	}

	@Test
	@DisplayName("선택한 게시글을 수정한다.")
	void updateArticle() {

		//given
		Article article = new Article(1, "작성 테스트", "내용입니다", "초롱이", LocalDateTime.now(), "abc1234");

		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("수정입니다.")
			.content("수정 내용 테스트")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		when(boardRepository.findById(1L)).thenReturn(Optional.of(article));

		//when
		ArticleResDto articleResDto = boardService.updateArticleById(1, reqDto);

		//then
		assertThat(article.getSubject()).isEqualTo("수정입니다.");
		assertThat(article.getContent()).isEqualTo("수정 내용 테스트");
		assertThat(article.getPassword()).isEqualTo("abc1234");
	}

	@Test
	@DisplayName("게시글 수정 - 패스워드 일치하지 않음")
	void no_match_password() {

		//given
		Article article = mock(Article.class);

		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("수정입니다.")
			.content("수정 내용 테스트")
			.writerName("초롱이")
			.password("abc1234")
			.build();

		//when
		when(boardRepository.findById(1L)).thenReturn(Optional.of(article));
		doReturn(false).when(article).isEqualPassword(anyString());

		//then
		assertThatThrownBy(() -> boardService.updateArticleById(1, reqDto))
			.isInstanceOf(InvalidPasswordException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("게시글 삭제")
	void deleteArticle() {

		//given
		Article article = new Article(1, "작성 테스트", "내용입니다", "초롱이", LocalDateTime.now(), "abc1234");
		PasswordReqDto passwordReqDto = new PasswordReqDto("abc1234");

		when(boardRepository.findById(1L)).thenReturn(Optional.of(article));

		//when
		boardService.deleteArticleById(1L, passwordReqDto);

		//then
		verify(boardRepository).deleteById(1L); // deleteById가 호출되었는지 확인
		when(boardRepository.findById(1L)).thenReturn(Optional.empty()); // 삭제 후 Optional.empty()를 반환하도록 mock 설정
		assertThat(boardRepository.findById(1L)).isEmpty();  // 삭제 후 존재하지 않아야 함
	}

}