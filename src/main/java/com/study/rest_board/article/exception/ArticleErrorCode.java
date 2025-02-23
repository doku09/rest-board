package com.study.rest_board.article.exception;

import com.study.rest_board.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ArticleErrorCode implements ErrorCode {
	ARTICLE_NOT_FOUND("게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD("비밀번호가 일치하지 않습니다.",HttpStatus.BAD_REQUEST),
	COMMENT_NOT_FOUND("댓글이 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	INVALID_WRITER("작성한 사용자가 아닙니다.",HttpStatus.BAD_REQUEST);

	private final String message;
	private final HttpStatus httpStatus;
}
