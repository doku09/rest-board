package com.study.rest_board.article.repository;

import com.study.rest_board.article.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<ArticleComment, Long> {

}
