package com.study.rest_board.entity;

import com.study.rest_board.dto.reqdto.ArticleSaveReqDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String subject;
	private String content;
	private String writerName;
	private LocalDateTime regDt;
	private String password;


	public void update(ArticleSaveReqDto reqDto) {
		this.subject = reqDto.getSubject();
		this.content = reqDto.getContent();
		this.writerName = reqDto.getWriterName();
		this.password = reqDto.getPassword();
	}

	public boolean isEqualPassword(String password) {
		return this.password.equals(password);
	}
}
