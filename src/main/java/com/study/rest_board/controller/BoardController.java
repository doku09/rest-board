package com.study.rest_board.controller;

import com.study.rest_board.dto.reqdto.PasswordReqDto;
import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
}
