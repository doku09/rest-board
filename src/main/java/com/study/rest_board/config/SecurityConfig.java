package com.study.rest_board.config;

import com.study.rest_board.config.jwt.JwtAuthenticationFilter;
import com.study.rest_board.config.jwt.JwtAuthorizationFilter;
import com.study.rest_board.user.repository.UserRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepositoryJPA userRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);

		// 로그인 경로를 /auth/login으로 변경
		jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");

		http.formLogin(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(AbstractHttpConfigurer::disable)
			.addFilter(jwtAuthenticationFilter)
			.addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository))
			.authorizeHttpRequests((authz) -> authz
//				.requestMatchers("/board/**").authenticated()
				.requestMatchers("/admin/article/**").hasAnyRole("ADMIN")
//				.requestMatchers("/board/**").hasRole()
//				.anyRequest().authenticated()
				.anyRequest().permitAll());


		http.addFilter(jwtAuthenticationFilter);
		return http.build();
	}

//	public class Custom extends AbstractHttpConfigurer<Custom, HttpSecurity> {
//		@Override
//		public void configure(HttpSecurity builder) throws Exception {
//			AuthenticationManager authenticationmanager = builder.getSharedObject(AuthenticationManager.class);
//			JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationmanager);
//			jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
//			builder.addFilter(jwtAuthenticationFilter);
//		}
//	}
}
