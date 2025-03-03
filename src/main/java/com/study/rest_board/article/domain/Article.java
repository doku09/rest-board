package com.study.rest_board.article.domain;

import com.study.rest_board.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private long id;
	private String subject;
	private String content;
	private LocalDateTime regDt;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User writer;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ArticleComment> comments =new ArrayList<>();

	@Builder
	public Article(long id, String subject, String content, LocalDateTime regDt) {
		this.id = id;
		this.subject = subject;
		this.content = content;
		this.regDt = regDt;
	}

	public void update(String subject,String content) {
		this.subject = subject;
		this.content = content;
	}

	public void addComment(ArticleComment comment) {
		this.comments.add(comment);
		comment.associateWithArticle(this);
	}

	public boolean isSameWriter(long userId) {
		return this.getWriter().getId() == userId;
	}
}
