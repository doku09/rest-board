package com.study.rest_board.article.dto.resdto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WriterResDto {
	private long id;
	private String name;
}
