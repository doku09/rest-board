package com.study.rest_board.article.controller;

import com.study.rest_board.article.dto.reqdto.*;
import com.study.rest_board.article.dto.resdto.ArticleCommentResDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.service.ArticleService;
import com.study.rest_board.common.resolver.AuthUserInfo;
import com.study.rest_board.common.jwt.auth.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	public ArticleResDto saveArticle(@RequestBody ArticleSaveReqDto reqDto,@AuthUserInfo AuthUserDto authUser) {

		return articleService.saveArticle(reqDto,authUser);
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
	@PatchMapping("/article/{id}")
	public ArticleResDto updateArticleById(@PathVariable("id") long id, @RequestBody ArticleUpdateReqDto reqDto ,@AuthUserInfo AuthUserDto authUser) {
		return articleService.updateArticleById(id,reqDto,authUser);
	}

	/**
	 * 게시글 삭제
	 */
	@DeleteMapping("/article/{id}")
	public String deleteArticleById(@PathVariable("id") long id ,@AuthUserInfo AuthUserDto authUser) {
		articleService.deleteArticleById(id,authUser);
		return "ok";
	}

	/**
	 * 댓글 작성
	 */
	@PostMapping("/article/comment")
	public ArticleCommentResDto saveComment(@RequestBody ArticleCommentSaveReqDto reqDto, @AuthUserInfo AuthUserDto authUser) {
		return articleService.saveComment(reqDto,authUser);
	}

	/**
	 * 댓글 수정
	 */
	@PatchMapping("/article/comment/{id}")
	public ArticleCommentResDto updateComment(@PathVariable("id") long id, @RequestBody ArticleCommentUpdateReqDto reqDto, @AuthUserInfo AuthUserDto authUser) {
		return articleService.updateComment(id,reqDto,authUser);
	}

	/**
	 * 댓글 삭제
	 */
	@DeleteMapping("/article/comment/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable("id") long id,  @AuthUserInfo AuthUserDto authUser) {

		String result = articleService.deleteCommentById(id, authUser);
		return ResponseEntity.ok(result);
	}
}
