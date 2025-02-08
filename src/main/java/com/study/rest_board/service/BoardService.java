package com.study.rest_board.service;

import com.study.rest_board.dto.ErrorResDto;
import com.study.rest_board.dto.resdto.ArticleResDto;
import com.study.rest_board.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.entity.Article;
import com.study.rest_board.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional
	public ArticleResDto saveArticle(ArticleSaveReqDto reqDto) {
		Article savedArticle = boardRepository.save(reqDto.of());
		return ArticleResDto.from(savedArticle);
	}

	public ArticleResDto findArticleById(long id) {
		Article findArticle = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
		return ArticleResDto.from(findArticle);
	}

	@Transactional
	public ArticleResDto updateArticleById(long id, ArticleSaveReqDto reqDto) {

			Article article = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
			if(article.checkWriter(reqDto.getWriterName(), reqDto.getPassword())){
				article.update(reqDto);
			} else {
				throw new IllegalArgumentException("잘못된 요청입니다.");
			}
		return ArticleResDto.from(article);
	}
}
