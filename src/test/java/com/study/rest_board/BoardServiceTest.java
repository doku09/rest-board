package com.study.rest_board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class BoardServiceTest {

	@Test
	void 계산() {
		Assertions.assertThat(1).isEqualTo(1);
	}
}
