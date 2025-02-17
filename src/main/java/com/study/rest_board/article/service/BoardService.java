package com.study.rest_board.article.service;

import com.study.rest_board.article.dto.reqdto.PasswordReqDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.article.domain.Article;
import com.study.rest_board.article.exception.ArticleNotFoundException;
import com.study.rest_board.article.exception.InvalidPasswordException;
import com.study.rest_board.article.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
		Article findArticle = boardRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));
		return ArticleResDto.from(findArticle);
	}

	@Transactional
	public ArticleResDto updateArticleById(long id, ArticleSaveReqDto reqDto) {

		Article article = boardRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));
		if (!article.isEqualPassword(reqDto.getPassword())) {
			throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
		}

		article.update(reqDto);
		return ArticleResDto.from(article);
	}

	public List<ArticleResDto> findAllArticles() {
		List<Article> articles = boardRepository.findAll();
		return articles.stream().map(ArticleResDto::from).toList();
	}

	@Transactional
	public void deleteArticleById(long id, PasswordReqDto authDto) {

		Article article = boardRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException("게시글이 존재하지 않습니다."));
		if (!article.isEqualPassword(authDto.getPassword())) {
			throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
		}
		boardRepository.deleteById(article.getId());
	}
}
