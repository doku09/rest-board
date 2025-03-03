package com.study.rest_board.article.service;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.article.dto.reqdto.*;
import com.study.rest_board.article.dto.resdto.ArticleCommentResDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.exception.ArticleErrorCode;
import com.study.rest_board.article.repository.ArticleRepository;
import com.study.rest_board.article.repository.CommentRepository;
import com.study.rest_board.common.UserRole;
import com.study.rest_board.common.exception.GlobalBusinessException;
import com.study.rest_board.common.jwt.auth.AuthUserDto;
import com.study.rest_board.user.UserSteps;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.repository.UserRepository;
import com.study.rest_board.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static com.study.rest_board.article.ArticleSteps.*;
import static com.study.rest_board.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class
ArticleServiceTest {

	@Mock
	ArticleRepository articleRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	CommentRepository commentRepository;
	@InjectMocks
	private ArticleService articleService;
	private LocalDateTime fixedDate = LocalDateTime.of(2025, Month.JANUARY,5,16,52);

	@Test
	@DisplayName("게시글 작성")
	void saveArticleTest() {

		// given
		Article article = new Article(1, "작성 테스트", "내용입니다",fixedDate);
		when(articleRepository.save(any(Article.class))).thenReturn(article);

		// when
		ArticleSaveReqDto reqDto = ArticleSaveReqDto.builder()
			.subject("작성 테스트")
			.content("내용입니다")
			.build();

		ArticleResDto articleResDto = articleService.saveArticle(reqDto,any(AuthUserDto.class));

		// then
		assertThat(articleResDto.getId()).isEqualTo(article.getId());
		assertThat("작성 테스트").isEqualTo(articleResDto.getSubject());
		assertThat("내용입니다").isEqualTo(articleResDto.getContent());

	}

	@Test
	@DisplayName("선택한 게시글 조회에 성공한다.")
	void findArticleById() {

		// given
		Article article = new Article(1, "작성 테스트", "내용입니다", LocalDateTime.now());

		// when
		when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

		// then
		ArticleResDto viewResDto = articleService.findArticleById(1);

		assertThat(1).isEqualTo(viewResDto.getId());
		assertThat("작성 테스트").isEqualTo(viewResDto.getSubject());
		assertThat("내용입니다").isEqualTo(viewResDto.getContent());
	}

	@Test
	@DisplayName("아이디로 게시글 선택조회시 아이디에 해당하는 게시글이 없으면 실패한다.")
	void failFindArticleTest() {

		//given
		when(articleRepository.findById(5L)).thenReturn(Optional.empty());
		//when
		//then
		assertThatThrownBy(() -> articleService.findArticleById(5L))
			.isInstanceOf(GlobalBusinessException.class);
	}

	@Test
	@DisplayName("선택한 게시글을 수정한다.")
	void updateArticle() {

		//given
		Article article = new Article(1, "작성 테스트", "내용입니다", fixedDate);
		article.setWriter(User.builder().id(1L).build());

		ArticleUpdateReqDto reqDto = 수정게시글_요청_DTO();

		when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

		//when
		ArticleResDto articleResDto = articleService.updateArticleById(1, reqDto,AuthUserDto.builder().id(1L).build());

		//then
		assertThat(articleResDto.getSubject()).isEqualTo("수정입니다.");
		assertThat(articleResDto.getContent()).isEqualTo("수정 내용 테스트");
	}

	@Test
	@DisplayName("게시글 수정 - 작성자가 일치하지않음")
	void no_match_password() {

		//given
		Article article = spy(Article.class);
		article.setWriter(User.builder().id(1L).build());

		ArticleUpdateReqDto reqDto = 수정게시글_요청_DTO();

		//when
		when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

		//then
		assertThatThrownBy(() -> articleService.updateArticleById(1, reqDto,AuthUserDto.builder().id(2L).build()))
			.isInstanceOf(GlobalBusinessException.class)
			.hasMessage(ArticleErrorCode.INVALID_WRITER.getMessage());
	}



	@Test
	@DisplayName("관리자는 모든 게시글을 수정할 수 있다.")
	void 게시글_수정_관리자() {
		
	  //given
		Article article = new Article(1, "작성 테스트", "내용입니다", fixedDate);
		article.setWriter(User.builder().id(1L).build());

		ArticleUpdateReqDto reqDto = 수정게시글_요청_DTO();
	  //when
		when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
	  //then
		ArticleResDto articleResDto = articleService.updateArticleById(1, reqDto, 관리자_권한_유저());

		assertThat(articleResDto).isNotNull();
		assertThat(articleResDto.getSubject()).isEqualTo("수정입니다.");
	}

	@Test
	@DisplayName("게시글 삭제")
	void deleteArticle() {

		//given
		Article article = new Article(1, "작성 테스트", "내용입니다", LocalDateTime.now());
		AuthUserDto userDto = AuthUserDto.builder().id(1L).build();

		when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

		//when
		articleService.deleteArticleById(1L, userDto);

		//then
		verify(articleRepository).deleteById(1L); // deleteById가 호출되었는지 확인
		when(articleRepository.findById(1L)).thenReturn(Optional.empty()); // 삭제 후 Optional.empty()를 반환하도록 mock 설정
		assertThat(articleRepository.findById(1L)).isEmpty();  // 삭제 후 존재하지 않아야 함
	}

	@Test
	@DisplayName("작성자가 아니면 게시글을 삭제할 수 없다.")
	void 게시글삭제_작성자가아닌경우() {

	  //given
		long deleteArticleId = 1L;
		Article article = 게시글_엔터티_생성();

		//when
		when(articleRepository.findById(deleteArticleId)).thenReturn(
			Optional.of(article)
		);

	  //then
		assertThatThrownBy(() -> articleService.deleteArticleById(deleteArticleId, 일반권한유저_생성(2L)))
			.isInstanceOf(GlobalBusinessException.class)
			.hasMessage(ArticleErrorCode.INVALID_WRITER.getMessage());
	}



	@Test
	@DisplayName("게시글 댓글을 작성한다")
	void 댓글_작성() {

	  //given
		ArticleCommentSaveReqDto reqDto = new ArticleCommentSaveReqDto("content", 1L, 1L);

		long articleId = 1L;
		when(articleRepository.findById(articleId)).thenReturn(Optional.of(new Article(1L,"subject","content",null)));

		long userId = 1L;
		AuthUserDto userDto = AuthUserDto.builder().id(1L).build();

		ArticleComment articleComment = new ArticleComment(1L,"comment content",null,null);
		when(commentRepository.save(any(ArticleComment.class)))
			.thenReturn(articleComment);

		//when
		ArticleCommentResDto result = articleService.saveComment(reqDto,userDto);

		//then
		Assertions.assertThat(result.getContent()).isEqualTo("comment content");

		verify(articleRepository).findById(articleId);  // boardRepository가 호출되었는지 검증
		verify(userRepository).findById(userId);  // userRepository가 호출되었는지 검증
		verify(commentRepository).save(any(ArticleComment.class));  // commentRepository가 호출되었는지 검증
	}
	
	@Test
	@DisplayName("작성한 게시글을 수정한다")
	void 게시글_수정_정상() {

	  //given
		long commentId = 1L;

		ArticleCommentUpdateReqDto updateReqDto = new ArticleCommentUpdateReqDto("updateContent", 1, 1);

		User findUser = new User(1L, "", "", null);

		Article findArticle = new Article(1L, "", "", LocalDateTime.now());

		ArticleComment savedComment = new ArticleComment(commentId, "originalContent", findUser, findArticle);

		when(commentRepository.findById(commentId))
			.thenReturn(Optional.of(savedComment));

		//when
		ArticleCommentResDto updatedComment = articleService.updateComment(commentId, updateReqDto,AuthUserDto.builder().id(1L).build());

		//then
		assertThat(updateReqDto.getContent()).isEqualTo(updatedComment.getContent());
	}

	@Test
	@DisplayName("작성한 댓글을 삭제한다.")
	void 댓글_삭제() {

	  //given
		long commentId = 1L;
		User writer = new User(1L, "dong", "", null);
		Article article = new Article(1L, "", "", LocalDateTime.now());
		ArticleComment articleComment = new ArticleComment(1L, "delete", writer, article);

		when(commentRepository.findById(commentId)).thenReturn(Optional.of(articleComment));

	  //when
		String result = articleService.deleteCommentById(commentId, AuthUserDto.builder().id(1L).build());
		when(commentRepository.findById(1L)).thenReturn(Optional.empty());
	  //then
		assertThat(result).isEqualTo("delete ok");
		assertThat(commentRepository.findById(commentId)).isNotPresent();
	}

	@Test
	@DisplayName("관리자는 모든 댓글을 삭제할 수 있다.")
	void 관리자_댓글_삭제() {

	  //given
		when(commentRepository.findById(1L)).thenReturn(
			Optional.of(
				댓글_엔터티_생성(1L)
			)
		);

	  //when
		articleService.deleteCommentById(1L, 관리자_권한_유저());
		verify(commentRepository).deleteById(1L);

	  //then
		when(commentRepository.findById(1L)).thenReturn(Optional.empty());
		assertThat(commentRepository.findById(1L)).isEqualTo(Optional.empty());
	}



}