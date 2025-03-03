package com.study.rest_board.article.repository;

import com.study.rest_board.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query("select a from Article a join fetch a.comments order by a.regDt desc")
	List<Article> findAllArticleWithComment();
}
