# ably-api
## 프로젝트 설명
- 찜 서랍과 찜 상품 목록을 다루는 API 서버

## 주요 기능
- HandlerInterceptor 를 이용한 JWT 토큰 처리
- HandlerMethodArgumentResolver 를 이용한 유저 정보 처리
- 찜 서랍 <-> 찜 상품 관계 매핑

## 사용 기술
- Kotlin, Spring Boot, Spring Data JPA, Docker, TestContainer

## API 명세
- http://localhost:8080/api/swagger-ui/index.html#/
- ably.http 파일을 통한 로컬 API 테스트 

## 로컬 실행 방법
- IntelliJ 설정
![img.png](img.png)
- cli 에서 명령어 실행
```docker-compose up -d```
- ably.http 파일을 이용하여 API 로컬 테스트


## 최종 구현된 범위
- 유저 이메일, 비밀번호 회원가입 및 로그인
- 로그인 후 유저 정보 조회
- 찜 서랍 생성, 삭제
- 찜 서랍 페이지네이션 조회
- 찜 서랍 중복 생성 방지
- 찜 서랍에 상품 찜 설정, 해제
- 찜 서랍의 찜 상품 페이지네이션 조회
- 찜 상품 중복 설정 방지
- 유효하지 않은 찜 서랍에 상품 찜 설정 방지




