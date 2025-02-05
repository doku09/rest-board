package com.study.rest_board.service;

import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.resdto.ArticleSaveReqDto;
import com.study.rest_board.entity.Article;
import com.study.rest_board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	public ArticleResDto saveArticle(ArticleSaveReqDto reqDto) {
		Article savedArticle = boardRepository.save(reqDto.toEntity());
		return savedArticle.of();
	}

	public ArticleResDto findArticleById(Long id) {
		Article findArticle = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
		return findArticle.of();
	}
}
