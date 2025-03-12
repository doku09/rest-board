package com.study.rest_board.common.config;

import com.study.rest_board.common.jwt.LoginFilter;
//import com.study.rest_board.common.jwt.JwtAuthorizationFilter;
import com.study.rest_board.common.jwt.JWTUtil;
import com.study.rest_board.common.jwt.JWTFilter;
import com.study.rest_board.common.jwt.auth.CustomLogoutFilter;
import com.study.rest_board.common.jwt.refresh.RefreshRepository;
import com.study.rest_board.user.repository.UserRepository;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserRepository userRepository;
	private final JWTUtil jwtUtil;
	private final RefreshRepository refreshRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager, AuthenticationConfiguration authenticationConfiguration) throws Exception {

		LoginFilter loginFilter = new LoginFilter(authenticationManager,jwtUtil,refreshRepository);
		
		loginFilter.setFilterProcessesUrl("/auth/login");

		http
			.formLogin(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable);

		http
			.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http
			.httpBasic(AbstractHttpConfigurer::disable);

		http
			.addFilterBefore(new JWTFilter(jwtUtil,userRepository), LoginFilter.class)
			.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new CustomLogoutFilter(jwtUtil,refreshRepository), LogoutFilter.class)
		;

		http
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers("/articles").authenticated()
//				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/reissue").permitAll()
				.anyRequest().permitAll());


		return http.build();
	}
}
