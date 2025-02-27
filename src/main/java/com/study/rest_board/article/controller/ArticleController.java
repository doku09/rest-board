package com.study.rest_board.article.controller;

import com.study.rest_board.article.dto.reqdto.*;
import com.study.rest_board.article.dto.resdto.ArticleCommentResDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.service.ArticleService;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;



	/**
	 * 게시글 전체 목록
	 */
	@GetMapping("/articles")
	public List<ArticleResDto> findArticles() {
		return articleService.findAllArticles();
	}

	/**
	 * 게시글 작성
	 */
	@PostMapping("/article")
	public ArticleResDto saveArticle(@RequestBody ArticleSaveReqDto reqDto) {
		return articleService.saveArticle(reqDto);
	}

	/**
	 * 게시글 조회
	 */
	@GetMapping("/article/{id}")
	public ArticleResDto findArticleById(@PathVariable("id") Long id) {
		return articleService.findArticleById(id);
	}

	/**
	 * 게시글 수정
	 */
	@PutMapping("/article/{id}")
	public ArticleResDto updateArticleById(@PathVariable("id") long id, @RequestBody ArticleUpdateReqDto reqDto) {
		return articleService.updateArticleById(id,reqDto);
	}

	/**
	 * 게시글 삭제
	 */
	@DeleteMapping("/article/{id}")
	public String deleteArticleById(@PathVariable("id") long id, @RequestBody PasswordReqDto passwordDto) {
		articleService.deleteArticleById(id,passwordDto);
		return "ok";
	}

	/**
	 * 댓글 작성
	 */
	@PostMapping("/article/comment")
	public ArticleCommentResDto saveComment(@RequestBody ArticleCommentSaveReqDto reqDto, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();
		reqDto.setUserId(user.getId());

		return articleService.saveComment(reqDto);
	}

	/**
	 * 댓글 수정
	 */
	@PatchMapping("/article/comment/{id}")
	public ArticleCommentResDto updateComment(@PathVariable("id") long id, @RequestBody ArticleCommentUpdateReqDto reqDto, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();
		reqDto.setUserId(user.getId());

		return articleService.updateComment(id,reqDto);
	}

	/**
	 * 댓글 삭제
	 */
	@DeleteMapping("/article/comment/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();

		String result = articleService.deleteCommentById(id, user.getId());
		return ResponseEntity.ok(result);
	}
}
