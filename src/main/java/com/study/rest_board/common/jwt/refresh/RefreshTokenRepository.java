package com.study.rest_board.common.jwt.refresh;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Boolean existsByRefresh(String refresh);

	@Transactional
	void deleteByRefresh(String refresh);
}
