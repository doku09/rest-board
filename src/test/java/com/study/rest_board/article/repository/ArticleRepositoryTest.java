package com.study.rest_board.article.repository;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.article.dto.reqdto.ArticleCommentSaveReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleCommentUpdateReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ArticleRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private CommentRepository commentRepository;

	@Test
	@DisplayName("Repository 의존성이 주입되었는지 확인한다")
	void 의존성_확인() {

		assertThat(articleRepository).isNotNull();
	}

  @Test
  @DisplayName("댓글 작성에 성공한다.")
  void 댓글_작성_성공() {


		//given
	  ArticleComment savedComment = saveComment();

	  //then
	  assertThat(savedComment).isNotNull();
		assertThat(savedComment.getId()).isEqualTo(1L);
		assertThat(savedComment.getContent()).isEqualTo("comment");
  }

	@Test
	@DisplayName("작성한 댓글을 수정한다")
	@Transactional
	void 댓글_수정_정상() {

	  //given
		ArticleComment savedComment = saveComment();
		Optional<ArticleComment> findComment = commentRepository.findById(savedComment.getId());

		assertThat(findComment).isPresent();

		ArticleCommentUpdateReqDto upateComment = ArticleCommentUpdateReqDto.builder()
			.articleId(1L)
			.content("update comment")
			.build();

		//when
		findComment.get().updateComment(upateComment.getContent());
//		commentRepository.flush();

		//then
		Optional<ArticleComment> updatedComment = commentRepository.findById(findComment.get().getId());
		assertThat(updatedComment).isPresent();
		assertThat(updatedComment.get()).isNotNull();
		assertThat(updatedComment.get().getContent()).isEqualTo("update comment");

	}

	@Test
	@DisplayName("댓글을 삭제한다")
	@Transactional
	void 댓글_삭제_정상() {
		
	  //given
		ArticleComment savedComment = saveComment();
		Optional<ArticleComment> findComment = commentRepository.findById(savedComment.getId());
		assertThat(findComment).isPresent();

		//when
		commentRepository.deleteById(savedComment.getId());
//		boardRepository.flush();

	  //then
		Optional<ArticleComment> deletedComment = commentRepository.findById(savedComment.getId());
		assertThat(deletedComment).isNotPresent();
		assertThat(deletedComment).isEqualTo(Optional.empty());
	}
	
	private ArticleComment saveComment() {
		//given
		ArticleCommentSaveReqDto reqDto = new ArticleCommentSaveReqDto("comment", 1L, 1L);

		User savedUser = userRepository.save(UserJoinRequestDto.builder().username("").build().toEntity());
		Article savedArticle = articleRepository.save(ArticleSaveReqDto.of("subejc","").toEntity());

		//when
		ArticleComment comment = reqDto.toEntity(savedArticle, savedUser);

		return commentRepository.save(comment);
	}
}