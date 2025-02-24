package com.study.rest_board.article.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest //>> 이거만 있어도된다.
//@DataJpaTest
//@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import(BoardRepository.class) //>> 안됨
//@ExtendWith(SpringExtension.class)
//@EnableJpaRepositories(basePackages = "com.study.rest_board.article.repository")
//@EntityScan(basePackages = "com.study.rest_board.article.domain")
class BoardRepositoryTest {
	@Autowired
	private BoardRepository boardRepository;

	@Test
	@DisplayName("Repository 의존성이 주입되었는지 확인한다")
	void 의존성_확인() {

		assertThat(boardRepository).isNotNull();
	}

  
}