package com.study.rest_board.article.repository;

import com.study.rest_board.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Article, Long> {
}
