package com.study.rest_board.article.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.rest_board.article.dto.reqdto.ArticleCommentSaveReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleCommentUpdateReqDto;
import com.study.rest_board.article.dto.reqdto.ArticleSaveReqDto;
import com.study.rest_board.article.dto.reqdto.PasswordReqDto;
import com.study.rest_board.article.dto.resdto.ArticleCommentResDto;
import com.study.rest_board.article.dto.resdto.ArticleResDto;
import com.study.rest_board.article.service.BoardService;
import com.study.rest_board.common.UserRole;
import com.study.rest_board.common.jwt.auth.PrincipalDetails;
import com.study.rest_board.user.domain.User;
import com.study.rest_board.user.dto.response.UserResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
@WithMockUser(username = "user1", roles = "USER")
class BoardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BoardService boardService;

	@Autowired
	private ObjectMapper objectMapper;

	private PrincipalDetails principalDetails;

	@BeforeEach
	void createUser() {
		User user = new User(1, "username", "abc123!", UserRole.ROLE_USER);
		principalDetails = new PrincipalDetails(user);

		// SecurityContext에 사용자 설정
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
			principalDetails, null, principalDetails.getAuthorities()
		));

		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("게시글 전체 목록 조회")
	void findArticles() throws Exception {
		// Given
		List<ArticleResDto> articles = Arrays.asList(
			ArticleResDto.of(1L, "제목1", "내용1", "작성자1"),
			ArticleResDto.of(2L, "제목2", "내용2", "작성자2")
		);
		when(boardService.findAllArticles()).thenReturn(articles);

		// When & Then
		mockMvc.perform(get("/board/articles"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(2))
			.andExpect(jsonPath("$[0].subject").value("제목1"))
			.andExpect(jsonPath("$[1].subject").value("제목2"));
	}

	@Test
	@DisplayName("게시글 작성")
	void saveArticle() throws Exception {
		// Given
		ArticleSaveReqDto requestDto = ArticleSaveReqDto.of("새 글 제목", "새 글 내용", "작성자");
		ArticleResDto responseDto = ArticleResDto.of(1L, "새 글 제목", "새 글 내용", "작성자");

		when(boardService.saveArticle(any(ArticleSaveReqDto.class))).thenReturn(responseDto);

		// When & Then
		mockMvc.perform(post("/board/article")
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf())
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.subject").value("새 글 제목"))
			.andExpect(jsonPath("$.content").value("새 글 내용"));
	}

	@Test
	@DisplayName("게시글 조회")
	void findArticleById() throws Exception {
		// Given
		Long articleId = 1L;
		ArticleResDto responseDto = ArticleResDto.of(articleId, "제목1", "내용1", "작성자1");

		when(boardService.findArticleById(articleId)).thenReturn(responseDto);

		// When & Then
		mockMvc.perform(get("/board/article/{id}", articleId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.subject").value("제목1"))
			.andExpect(jsonPath("$.content").value("내용1"));
	}

	@Test
	@DisplayName("게시글 수정")
	void updateArticleById() throws Exception {
		// Given
		Long articleId = 1L;
		ArticleSaveReqDto requestDto = ArticleSaveReqDto.of("수정된 제목", "수정된 내용", "작성자");
		ArticleResDto responseDto = ArticleResDto.of(articleId, "수정된 제목", "수정된 내용", "작성자");

		when(boardService.updateArticleById(eq(articleId), any(ArticleSaveReqDto.class)))
			.thenReturn(responseDto);

		// When & Then
		mockMvc.perform(put("/board/article/{id}", articleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.subject").value("수정된 제목"))
			.andExpect(jsonPath("$.content").value("수정된 내용"));
	}

	@Test
	@DisplayName("게시글 삭제")
	void deleteArticleById() throws Exception {
		// Given
		Long articleId = 1L;
		PasswordReqDto passwordDto = new PasswordReqDto("password");

		Mockito.doNothing().when(boardService).deleteArticleById(eq(articleId), any(PasswordReqDto.class));

		// When & Then
		mockMvc.perform(delete("/board/article/{id}", articleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(passwordDto)))
			.andExpect(status().isOk())
			.andExpect(content().string("ok"));
	}

	@Test
	@DisplayName("게시글 댓글 작성을 요청한다. - 정상")
	void 댓글_작성() throws Exception {

		//given
		ArticleCommentSaveReqDto reqDto = new ArticleCommentSaveReqDto("댓글 작성하기", 1, 1);

		when(boardService.saveComment(any(ArticleCommentSaveReqDto.class)))
			.thenReturn(
			new ArticleCommentResDto("댓글 작성하기",
				new ArticleResDto(1, "", "", "", "", ""),
				null
			)
		);

		//then
		mockMvc.perform(post("/board/article/comment")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reqDto))
				.with(SecurityMockMvcRequestPostProcessors.user(principalDetails))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("댓글 작성하기"))
			.andExpect(jsonPath("$.article.id").value(1));

	}
	
	@Test
	@DisplayName("작성한 댓글을 수정한다.")
	void 댓글_수정() throws Exception {
		
	  //given
		ArticleCommentUpdateReqDto updateReqDto = new ArticleCommentUpdateReqDto("댓글 수정", 1, 1);
		long commentId = 1L;
		//when
		when(boardService.updateComment(eq(1L),any(ArticleCommentUpdateReqDto.class))).thenReturn(new ArticleCommentResDto("댓글 수정",null,null));

	  //then
		mockMvc.perform(patch("/board/article/comment/" + commentId)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updateReqDto))
			.with(SecurityMockMvcRequestPostProcessors.user(principalDetails))
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value("댓글 수정"));
	}
	
	@Test
	@DisplayName("작성한 댓글 삭제하기")
	void 댓글_삭제() throws Exception {
		
	  //given
		long commentId = 1L;
		long userId = 1L;
		when(boardService.deleteCommentById(commentId,userId)).thenReturn("delete ok");

	  //when && then
		mockMvc.perform(
			delete("/board/article/comment/" + commentId)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(principalDetails))
		)
			.andExpect(status().isOk())
			.andExpect(content().string("delete ok"));
	}

}