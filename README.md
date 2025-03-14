# 📑뻔뻔한 게시판 API  프로젝트

---
## 개요


- 기본적인 게시판 CRUD 프로젝트입니다. 
- 학습한 내용을 추가하고 리팩토링하였습니다.
- 계층별 단위 테스트 코드를 작성하며 코드 커버리지를 높이고 유지보수가 쉬운 서비스를 만들려고 합니다.  
---
## 개발 환경

- Java 17
- SpringBoot 3.4.2
- gradle
- JPA
- H2 Database 2.1.214
---
## 기능

| 카테고리 | 기능 | 
|--------|-------|
| 게시판 | 전체 게시글 목록 조회 | 
|        | 게시글 작성 | 
|        | 선택한 게시글 조회 | 
|        | 게시글 수정 | 
|        | 게시글 삭제 | 
|  댓글 |  댓글 작성 | 
|        | 댓글 수정 | 
|        | 댓글 삭제 | 
| 인증 | 회원가입 | 
|        | 로그인 | 
---
## 프로젝트 기술
- Filter
  - LoginFilter
  - JWTFilter
- JWT
  - Refresh token
- Spring ArgumentsResolver
- Mockito

## 그외

