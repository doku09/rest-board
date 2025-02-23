package com.study.rest_board.article.controller;

import com.study.rest_board.article.domain.ArticleComment;
import com.study.rest_board.article.dto.reqdto.ArticleCommentSaveReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleCommentUpdateReqDto;
import com.study.rest_board.article.dto.reqdto.PasswordReqDto;
import com.study.rest_board.article.dto.resdto.ArticleCommentResDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.article.service.BoardService;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;



	/**
	 * 게시글 전체 목록
	 */
	@GetMapping("/articles")
	public List<ArticleResDto> findArticles() {
		return boardService.findAllArticles();
	}

	/**
	 * 게시글 작성
	 */
	@PostMapping("/article")
	public ArticleResDto saveArticle(@RequestBody ArticleSaveReqDto reqDto) {
		return boardService.saveArticle(reqDto);
	}

	/**
	 * 게시글 조회
	 */
	@GetMapping("/article/{id}")
	public ArticleResDto findArticleById(@PathVariable("id") Long id) {
		return boardService.findArticleById(id);
	}

	/**
	 * 게시글 수정
	 */
	@PutMapping("/article/{id}")
	public ArticleResDto updateArticleById(@PathVariable("id") long id, @RequestBody ArticleSaveReqDto reqDto) {
		return boardService.updateArticleById(id,reqDto);
	}

	/**
	 * 게시글 삭제
	 */
	@DeleteMapping("/article/{id}")
	public String deleteArticleById(@PathVariable("id") long id, @RequestBody PasswordReqDto passwordDto) {
		boardService.deleteArticleById(id,passwordDto);
		return "ok";
	}

	/**
	 * 댓글 작성
	 */
	@PostMapping("/article/comment")
	public ArticleCommentResDto saveComment(@RequestBody ArticleCommentSaveReqDto reqDto, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();
		reqDto.setUserId(user.getId());

		return boardService.saveComment(reqDto);
	}

	/**
	 * 댓글 수정
	 */
	@PatchMapping("/article/comment/{id}")
	public ArticleCommentResDto updateComment(@PathVariable("id") long id, @RequestBody ArticleCommentUpdateReqDto reqDto, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();
		reqDto.setUserId(user.getId());

		return boardService.updateComment(id,reqDto);
	}

	/**
	 * 댓글 삭제
	 */
	@DeleteMapping("/article/comment/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principal) {

		User user = principal.getUser();

		String result = boardService.deleteCommentById(id, user.getId());
		return ResponseEntity.ok(result);
	}
}
