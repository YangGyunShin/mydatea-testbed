# 🐛 트러블슈팅 가이드

프로젝트 개발 중 발생한 버그와 해결 방법을 정리한 문서입니다.

---

## 목차

- [Spring Security 관련](#spring-security-관련)
- [Thymeleaf 관련](#thymeleaf-관련)
- [Spring MVC 관련](#spring-mvc-관련)
- [JPA 관련](#jpa-관련)
- [이메일 인증 관련](#이메일-인증-관련)

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

### 5. (예시) LazyInitializationException

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

### 6. Gmail SMTP 인증 실패

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

### 7. 이메일 인증 토큰이 만료됨

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
