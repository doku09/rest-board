package com.study.rest_board.user.domain;

import com.study.rest_board.common.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;
	@Setter
	private String username;
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role; //USER,ADMIN

	public void setRole(String role) {

		UserRole userRole = UserRole.ROLE_USER;

		if(role.equals(UserRole.ROLE_ADMIN.name())) {
			userRole = UserRole.ROLE_ADMIN;
		}
		this.role = userRole;
	}
}
