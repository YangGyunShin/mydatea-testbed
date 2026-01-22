# 🐛 트러블슈팅 가이드

프로젝트 개발 중 발생한 버그와 해결 방법을 정리한 문서입니다.

---

## 목차

- [Spring Security 관련](#spring-security-관련)
- [Thymeleaf 관련](#thymeleaf-관련)
- [Spring MVC 관련](#spring-mvc-관련)
- [JPA 관련](#jpa-관련)
- [이메일 인증 관련](#이메일-인증-관련)
- [데이터베이스 관련](#데이터베이스-관련)

---

## Spring Security 관련

### 1. 이메일 인증 링크 클릭 시 로그인 페이지로 리다이렉트됨

**증상**
- 이메일의 "이메일 인증하기" 버튼 클릭
- `/member/verify-email?token=xxx` URL로 이동해야 하는데
- `/member/login` 페이지로 자동 리다이렉트됨

**원인**
- Spring Security가 `/member/verify-email` URL을 인증이 필요한 페이지로 인식
- 로그인하지 않은 상태에서 접근하면 자동으로 로그인 페이지로 리다이렉트

**해결 방법**
`SecurityConfig.java`에서 해당 URL을 `permitAll()`에 추가:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/", "/intro/**", "/api-guide/**").permitAll()
            .requestMatchers(
                "/member/login",
                "/member/signup/**",
                "/member/verify-email",       // ✅ 추가
                "/member/resend-verification" // ✅ 추가
            ).permitAll()
            .anyRequest().authenticated()
        );
    return http.build();
}
```

**교훈**
- 로그인 없이 접근해야 하는 URL은 반드시 `permitAll()`에 추가할 것
- 이메일 인증, 비밀번호 재설정 등 비로그인 상태에서 접근하는 기능은 특히 주의

---

### 2. 로그인 시 "Bad credentials" 또는 "User is disabled" 오류

**증상**
- 올바른 이메일/비밀번호를 입력해도 로그인 실패
- 에러 메시지: "Bad credentials" 또는 "User is disabled"

**원인**
- `CustomUserDetails.isEnabled()` 메서드가 `member.isEmailVerified()`를 반환
- 이메일 인증을 완료하지 않은 회원은 `emailVerified = false`
- Spring Security는 `isEnabled() = false`인 계정의 로그인을 거부

```java
// CustomUserDetails.java
@Override
public boolean isEnabled() {
    return member.isEmailVerified();  // false면 로그인 불가!
}
```

**해결 방법**
1. **(권장)** 이메일 인증 기능을 완전히 구현하여 인증 완료 후 로그인 가능하도록 함
2. **(테스트용)** 임시로 `isEnabled()`가 항상 `true`를 반환하도록 수정

```java
// 테스트용 임시 해결 (운영에서는 사용 금지!)
@Override
public boolean isEnabled() {
    return true;  // 또는 member.isEmailVerified();
}
```

**교훈**
- Spring Security의 `UserDetails` 인터페이스는 여러 계정 상태를 체크함
- `isEnabled()`, `isAccountNonLocked()`, `isAccountNonExpired()`, `isCredentialsNonExpired()` 중 하나라도 `false`면 로그인 실패
- 이메일 인증 기능을 구현할 때는 이 연결고리를 먼저 이해해야 함

---

## Thymeleaf 관련

### 3. 브레드크럼에서 SpEL 오류 발생

**증상**
- 페이지 로딩 시 Thymeleaf 오류 발생
- 에러 메시지: `SpelEvaluationException` 또는 `Property or field 'name' cannot be found`

**원인**
- `breadcrumb.html` fragment에서 `${items}` 변수를 사용하는데
- Controller에서 해당 데이터를 Model에 담지 않음
- 또는 데이터 구조가 맞지 않음 (예: `name`, `url` 필드가 없는 객체 전달)

**해결 방법**
Controller에서 브레드크럼 데이터를 올바른 형식으로 전달:

```java
@GetMapping("/signup/step1")
public String signupStep1(Model model) {
    List<Map<String, String>> breadcrumbItems = List.of(
        Map.of("name", "로그인", "url", "/member/login"),
        Map.of("name", "회원가입", "url", "")  // 현재 페이지는 url 비움
    );
    model.addAttribute("breadcrumbItems", breadcrumbItems);
    return "member/signup-step1-terms";
}
```

**교훈**
- Thymeleaf fragment에서 사용하는 변수는 반드시 Controller에서 전달해야 함
- fragment의 파라미터 이름과 Model에 담는 이름이 일치해야 함

---

## Spring MVC 관련

### 4. 폼 데이터가 DTO에 바인딩되지 않음

**증상**
- 회원가입 폼 제출 시 `MemberSignupRequestDto`의 모든 필드가 `null`
- `@Valid` 검증에서 모든 필드가 실패

**원인**
- `MemberSignupRequestDto`에 `@Setter`가 없음
- Spring MVC는 폼 데이터 바인딩 시 기본 생성자로 객체를 만들고 Setter로 값을 주입
- `@Builder`만 있으면 Setter가 없어서 값이 주입되지 않음

**해결 방법**
RequestDto에는 `@Setter` 추가:

```java
@Getter
@Setter  // ✅ 추가!
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupRequestDto {
    private String email;
    private String password;
    // ...
}
```

**교훈**
- **Entity, ResponseDto**: `@Setter` 사용 금지, `@Builder`만 사용
- **RequestDto**: `@Setter` 필수 (Spring MVC 폼 바인딩을 위해)
- 프로젝트 컨벤션에서 "No Setter"는 Entity와 ResponseDto에만 적용

---

## JPA 관련

### 5. 공지사항 목록에서 작성자 조회 시 N+1 문제

**증상**
- 공지사항 목록 조회 시 쿼리가 N+1번 실행됨
- 10개 목록 조회 시 11번의 쿼리 발생 (목록 1번 + 작성자 조회 10번)
- 로그에 SELECT 쿼리가 여러 번 찍힘

**원인**
- `@ManyToOne`의 기본 FetchType이 `EAGER`
- LAZY로 바꿔도 Thymeleaf에서 `notice.author.name` 접근 시 각각 쿼리 발생
- 연관 엔티티를 개별적으로 조회하면서 쿼리 폭발

**해결 방법**
**방법 1: Fetch Join 사용 (권장)**
```java
// NoticeRepository.java
@Query("SELECT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.id = :id")
Optional<Notice> findByIdWithAuthor(@Param("id") Long id);
```

**방법 2: EntityGraph 사용**
```java
@EntityGraph(attributePaths = {"author"})
@Query("SELECT n FROM Notice n WHERE n.id = :id")
Optional<Notice> findByIdWithAuthor(@Param("id") Long id);
```

**방법 3: DTO Projection**
```java
@Query("SELECT new com.mydata.dto.NoticeListDto(n.id, n.title, m.name) " +
       "FROM Notice n LEFT JOIN n.author m")
List<NoticeListDto> findAllAsDto();
```

**교훈**
- `@ManyToOne`은 기본이 `EAGER`이므로 명시적으로 `LAZY` 지정 필요
- 목록 조회 시 연관 엔티티가 필요하면 Fetch Join 사용
- 상세 조회와 목록 조회에서 다른 쿼리 메서드 사용 권장

---

### 6. LazyInitializationException

**증상**
- 연관 엔티티 접근 시 `LazyInitializationException` 발생
- 에러 메시지: `could not initialize proxy - no Session`

**원인**
- `@ManyToOne(fetch = FetchType.LAZY)` 등으로 지연 로딩 설정된 연관 엔티티
- 트랜잭션 밖에서 해당 엔티티에 접근하려고 함

**해결 방법**
1. Service 메서드에 `@Transactional(readOnly = true)` 추가
2. 또는 Fetch Join 사용
3. 또는 DTO로 필요한 데이터만 조회

```java
// 방법 1: 트랜잭션 범위 확장
@Transactional(readOnly = true)
public MemberResponseDto getMember(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    return memberMapper.toResponseDto(member);
}

// 방법 2: Fetch Join
@Query("SELECT m FROM Member m JOIN FETCH m.company WHERE m.id = :id")
Optional<Member> findByIdWithCompany(@Param("id") Long id);
```

---

## 이메일 인증 관련

### 7. Gmail SMTP 인증 실패

**증상**
- 이메일 발송 시 `AuthenticationFailedException` 발생
- 에러 메시지: `535-5.7.8 Username and Password not accepted`

**원인**
- Gmail 일반 비밀번호 사용 (앱 비밀번호가 아님)
- 또는 2단계 인증이 활성화되지 않음

**해결 방법**
1. Google 계정에서 2단계 인증 활성화
2. 앱 비밀번호 생성: Google 계정 → 보안 → 2단계 인증 → 앱 비밀번호
3. "메일" 선택 후 생성된 16자리 비밀번호를 환경변수에 설정

```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=abcd efgh ijkl mnop  # 16자리 앱 비밀번호
```

**교훈**
- Gmail SMTP는 일반 비밀번호로는 인증 불가 (보안 정책)
- 반드시 앱 비밀번호를 생성해서 사용해야 함

---

### 8. 이메일 인증 토큰이 만료됨

**증상**
- 이메일 링크 클릭 시 "만료된 토큰입니다" 오류
- 24시간이 지나지 않았는데도 발생

**원인**
- 서버 시간과 DB 시간이 다름
- 또는 토큰 생성 시 시간 계산 오류

**해결 방법**
1. 서버와 DB의 타임존 설정 확인
2. `EmailVerificationToken` 엔티티의 시간 처리 로직 확인

```java
// application.yml에 타임존 설정
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          time_zone: Asia/Seoul
```

---

## 데이터베이스 관련

### 9. 회원가입 후 로그인 실패 - 서버 재시작 시 데이터 사라짐

**증상**
- 회원가입 완료 후 로그인 시 "없는 계정" 오류 발생
- 서버를 재시작하면 가입한 계정이 사라짐
- 회원가입을 다시 해도 동일한 문제 반복

**원인**
H2 메모리 DB를 사용하면 서버 종료 시 모든 데이터가 삭제됩니다.

```yaml
# 문제가 되는 설정
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # 메모리 DB - 서버 종료 시 데이터 삭제!
```

**해결 방법**
H2 파일 DB로 변경하여 데이터를 영구 저장:

```yaml
# application.yml
spring:
  datasource:
    # 파일 DB로 변경
    url: jdbc:h2:file:./data/testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: update  # create → update로 변경 (기존 데이터 유지)
```

**.gitignore에 data 폴더 추가:**
```gitignore
### H2 Database ###
data/
```

| 설정 | 설명 |
|------|------|
| `file:./data/testdb` | 프로젝트 루트의 `data` 폴더에 `testdb.mv.db` 파일로 저장 |
| `DB_CLOSE_DELAY=-1` | 마지막 연결이 닫혀도 DB 유지 |
| `DB_CLOSE_ON_EXIT=FALSE` | JVM 종료 시에도 DB 파일 유지 |
| `ddl-auto: update` | 기존 테이블/데이터 유지, 스키마 변경만 반영 |

**H2 콘솔 접속 정보:**
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/testdb
Username: sa
Password: (비워두기)
```

**교훈**
- 개발 환경에서도 데이터 유지가 필요하면 파일 DB 사용
- `mem:` = 메모리 DB (휘발성), `file:` = 파일 DB (영구)
- 운영 환경에서는 MySQL/PostgreSQL 등 실제 DBMS 사용

---

### 10. ddl-auto: create로 인한 데이터 손실

**증상**
- 서버 재시작 후 기존 데이터가 모두 삭제됨
- 테이블이 매번 새로 생성됨

**원인**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create  # 서버 시작 시 테이블 DROP 후 CREATE
```

**해결 방법**
개발 환경에서는 `update`, 운영 환경에서는 `validate` 또는 `none` 사용:

```yaml
# 개발 환경
ddl-auto: update  # 기존 테이블 유지, 변경사항만 반영

# 운영 환경
ddl-auto: validate  # 스키마 검증만, 변경하지 않음
```

| 옵션 | 동작 | 권장 환경 |
|------|------|----------|
| `create` | DROP + CREATE | 테스트용 |
| `create-drop` | CREATE + 종료 시 DROP | 테스트용 |
| `update` | 변경사항만 반영 | 개발 |
| `validate` | 검증만, 변경 안 함 | 운영 |
| `none` | 아무것도 안 함 | 운영 |

---

## MemberController 관련

### 11. 중복 회원 생성 버그

**증상**
- 회원가입 완료 버튼을 여러 번 누르면 중복 회원이 생성됨
- 또는 Step 4 POST에서 회원이 또 생성됨

**원인**
Step 3 POST와 Step 4 POST 양쪽에서 `memberService.signup()`을 호출

```java
// ❌ 잘못된 구조
@PostMapping("/signup/step3")
public String step3(...) {
    memberService.signup(requestDto);  // 여기서 생성
    return "redirect:/member/signup/step4";
}

@PostMapping("/signup/step4")
public String step4(...) {
    memberService.signup(requestDto);  // 또 생성! (중복)
    return "redirect:/member/signup/complete";
}
```

**해결 방법**
Step 3에서만 회원 생성, Step 4는 안내 페이지로만 사용:

```java
// ✅ 올바른 구조
@PostMapping("/signup/step3")
public String step3Process(...) {
    // 1. 회원 생성 (emailVerified = false)
    memberService.signup(requestDto);
    
    // 2. 인증 메일 발송
    emailService.sendVerificationEmail(requestDto.getEmail());
    
    return "member/signup-step4-email";  // Step 4 안내 페이지로 이동
}

// Step 4 POST는 제거하거나 주석 처리
// 이메일 링크 클릭 → /member/verify-email → 인증 완료
```

**올바른 회원가입 흐름:**
```
Step 3 POST → 회원 생성 + 메일 발송 → Step 4 (안내 페이지)
    ↓
사용자가 메일의 링크 클릭
    ↓
GET /member/verify-email?token=xxx → 인증 완료 → 로그인 가능
```

---

## 버그 보고 템플릿

새로운 버그를 발견하면 아래 형식으로 추가해주세요:

```markdown
### N. 버그 제목

**증상**
- 어떤 현상이 발생했는지

**원인**
- 왜 이런 현상이 발생했는지

**해결 방법**
코드 또는 설정 변경 내용

**교훈**
- 이 버그를 통해 배운 점
```

---

## 참고 자료

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Spring MVC Data Binding](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/modelattrib-method-args.html)
- [H2 Database Connection Modes](https://h2database.com/html/features.html#connection_modes)
