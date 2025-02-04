package com.study.rest_board.controller;

import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.resdto.ArticleSaveReqDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

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
	 * @param articleReqDto
	 * @return
	 */
	@PostMapping("/article")
	public ArticleResDto saveArticle(@RequestBody ArticleSaveReqDto articleReqDto) {
		return new ArticleResDto();
	}

	/**
	 * 선택한 게시글 조회
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/article/{id}")
	public ArticleResDto findArticleById(@PathVariable String id) {
		return new ArticleResDto();
	}

	/**
	 * 게시글 수정
	 *
	 * @param id
	 * @param articleUpdateReqDto
	 * @return
	 */
	@PutMapping("/article/{id}")
	public ArticleResDto updateArticleById(@PathVariable String id, @RequestBody ArticleSaveReqDto articleUpdateReqDto) {
		return new ArticleResDto();
	}

	/**
	 * 선택한 게시글 삭제
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/article/{id}")
	public String deleteArticleById(@PathVariable String id) {
		return "ok";
	}
}
