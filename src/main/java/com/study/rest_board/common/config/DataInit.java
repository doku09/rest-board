package com.study.rest_board.common.config;

import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.article.dto.reqdto.ArticleCommentSaveReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.article.repository.ArticleRepository;
import com.study.rest_board.article.repository.CommentRepository;
import com.study.rest_board.common.UserRole;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.request.UserJoinRequestDto;
import com.study.rest_board.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

@Component
public class DataInit {

	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	private LocalDateTime fixedDate = LocalDateTime.of(2025, Month.of(2),5,8,25);


	@PostConstruct
	void dataInit() {

		// 회원
		User user1 = new UserJoinRequestDto("user", "$2a$10$lWu7H7WadF2dhL3hYnlFYOva50t4VigtJsU4OvIfnbc.fqLFq5J4O", UserRole.ROLE_USER).toEntity();
		User user2 = new UserJoinRequestDto("admin", "$2a$10$lWu7H7WadF2dhL3hYnlFYOva50t4VigtJsU4OvIfnbc.fqLFq5J4O", UserRole.ROLE_ADMIN).toEntity();//abc1234!

		userRepository.save(user1);
		userRepository.save(user2);


		// 게시글
		Article article1 = new ArticleSaveReqDto("[인터뷰] 기아 PBV에 삼성 끼어든 이유… 新시장 개척", "내용", fixedDate).toEntity();
		Article article2 = new ArticleSaveReqDto("고준위법 국회 통과했지만…부지 선정·주민 반대 ", "내용", fixedDate).toEntity();
		Article article3 = new ArticleSaveReqDto("안 그래도 바늘구멍인데…1억 미만 가계대출도 소득 따진다", "내용", fixedDate).toEntity();

		article1.setWriter(User.builder()
			.id(1L)
			.build());
		article2.setWriter(User.builder()
			.id(2L)
			.build());
		article3.setWriter(User.builder()
			.id(1L)
			.build());


		// 댓글
		ArticleComment comment1 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article1, user1);
		ArticleComment comment2 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article1, user1);
		ArticleComment comment3 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article2, user2);
		ArticleComment comment4 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article2, user1);
		ArticleComment comment5 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article3, user2);
		ArticleComment comment6 = new ArticleCommentSaveReqDto("대박이네요", 1L, 1L).toEntity(article3, user1);

		article1.addComment(comment1);
		article1.addComment(comment2);
		article2.addComment(comment3);
		article2.addComment(comment4);
		article3.addComment(comment5);
		article3.addComment(comment6);

		articleRepository.save(article1);
		articleRepository.save(article2);
		articleRepository.save(article3);
	}
}
