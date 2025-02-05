package com.study.rest_board.controller;

import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.resdto.ArticleSaveReqDto;
import com.study.rest_board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	/**
	 * 게시글 전체 목록
	 *
	 * @return
	 */
	@GetMapping("/articles")
	public String findArticles() {
		return "test";
	}

	/**
	 * 게시글 작성
	 *
	 * @param reqDto
	 * @return
	 */
	@PostMapping("/article")
	public ArticleResDto saveArticle(@RequestBody ArticleSaveReqDto reqDto) {
		return boardService.saveArticle(reqDto);
	}

	/**
	 * 게시글 조회
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/article/{id}")
	public ArticleResDto findArticleById(@PathVariable("id") Long id) {
		return boardService.findArticleById(id);
	}

	/**
	 * 게시글 수정
	 *
	 * @param id
	 * @param reqDto
	 * @return
	 */
	@PutMapping("/article/{id}")
	public ArticleResDto updateArticleById(@PathVariable String id, @RequestBody ArticleSaveReqDto reqDto) {
		return new ArticleResDto();
	}

	/**
	 * 게시글 삭제
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/article/{id}")
	public String deleteArticleById(@PathVariable String id) {
		return "ok";
	}
}
