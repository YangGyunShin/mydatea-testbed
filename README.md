# 🏦 금융분야 마이데이터 테스트베드

> 금융보안원 마이데이터 테스트베드 클론 프로젝트  
> 원본 사이트: https://developers.mydatakorea.org/mdtb/

마이데이터 서비스 개발자를 위한 API 테스트 환경을 제공하는 웹 애플리케이션입니다.

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [아키텍처 & 코딩 컨벤션](#아키텍처--코딩-컨벤션)
- [시스템 아키텍처](#시스템-아키텍처)
- [프로젝트 구조](#프로젝트-구조)
- [메뉴 구조](#메뉴-구조)
- [ERD](#erd)
- [이메일 인증 플로우](#이메일-인증-플로우)
- [실행 방법](#실행-방법)
- [개발 로드맵](#개발-로드맵)

---

## 프로젝트 개요

### 주요 기능

| 기능 | 설명 |
|------|------|
| **테스트베드 소개** | 마이데이터 서비스 및 테스트베드 소개 |
| **API 가이드** | 데이터 표준 API 규격, 인증/지원/정보제공 API 규격 문서 |
| **테스트베드** | 마이데이터 서비스 테스트, API 서버 테스트 |
| **적합성 심사** | 기능적합성 심사, 보안취약점 결과 점검 |
| **고객지원** | 공지사항, FAQ, 문의하기, 자료실, 자유게시판 |
| **회원관리** | 회원가입(4단계), 로그인/로그아웃, 이메일 인증 |

---

## 기술 스택

### Backend
| 기술 | 버전 | 설명 |
|------|------|------|
| Java | 21 | 프로그래밍 언어 |
| Spring Boot | 3.4.1 | 웹 프레임워크 |
| Spring Security | 6.x | 인증/인가 (Form Login 방식) |
| Spring Data JPA | - | ORM |
| Spring Validation | - | Bean Validation (커스텀 어노테이션 포함) |
| Spring Mail | - | 이메일 발송 (Gmail SMTP) |
| Thymeleaf | - | 템플릿 엔진 |
| Thymeleaf Layout Dialect | - | 레이아웃 템플릿 |
| Lombok | - | 보일러플레이트 코드 제거 |

### Database
| 기술 | 용도 |
|------|------|
| H2 Database | 개발/테스트 환경 |
| MySQL | 운영 환경 |

### Frontend
| 기술 | 설명 |
|------|------|
| HTML5 / CSS3 | 마크업 & 스타일링 |
| JavaScript | 클라이언트 스크립트 |
| Thymeleaf | 서버사이드 템플릿 |

### Build & Tools
| 도구 | 설명 |
|------|------|
| Gradle | 빌드 도구 |
| IntelliJ IDEA | IDE |
| Git | 버전 관리 |

---

## 아키텍처 & 코딩 컨벤션

### 클린 아키텍처 원칙

| 규칙 | 설명 |
|------|------|
| **No Factory Method** | DTO, Entity, VO에 `of()`, `from()` 등 정적 팩토리 메서드 사용 금지 |
| **No Setter** | 모든 클래스에서 Setter 사용 금지, `@Builder` 패턴 사용 |
| **Use Mapper** | DTO ↔ Entity 변환은 별도 Mapper 클래스 사용 |
| **Use VO** | 핵심 값 객체(Email, Password, Phone)는 VO로 래핑하여 타입 안전성 확보 |
| **Domain Logic in Entity/VO** | 도메인 규칙은 Entity와 VO에 캡슐화 (예: 이메일 형식 검증은 VO에서) |
| **Use Case in Service** | Service는 유스케이스 조합만 담당, 인프라 의존성 조율 |

### 파일 네이밍 규칙

| 타입 | 접미사 | 예시 |
|------|--------|------|
| Entity | 도메인명만 | `Member.java` |
| VO | `Vo` | `EmailVo.java` |
| DTO (Request) | `RequestDto` | `MemberSignupRequestDto.java` |
| DTO (Response) | `ResponseDto` | `MemberResponseDto.java` |
| Mapper | `Mapper` | `MemberMapper.java` |
| Service Interface | `Service` | `MemberService.java` |
| Service 구현체 | `ServiceImpl` | `MemberServiceImpl.java` |

### Lombok 어노테이션 패턴

| 클래스 종류 | 어노테이션 | 이유 |
|------------|-----------|------|
| **Entity** | `@Getter @NoArgsConstructor(access = PROTECTED)` + 생성자에 `@Builder` | id, 시간 필드 제외 |
| **VO** | `@Getter @NoArgsConstructor(access = PROTECTED)` + 생성자에 `@Builder` | 검증 로직 포함 |
| **ResponseDto** | `@Getter @Builder` | Mapper에서 Builder로만 생성 |
| **RequestDto** | `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor` | Spring MVC 바인딩 필요 |

> **Note:** RequestDto에만 `@Setter`를 허용하는 이유는 Spring MVC가 폼 데이터를 바인딩할 때 기본 생성자로 객체를 만들고 Setter로 값을 주입하기 때문입니다. Entity나 ResponseDto는 Setter 없이 Builder 패턴만 사용합니다.

---

## 시스템 아키텍처
```
┌─────────────────────────────────────────────────────────────────┐
│                          Client (Browser)                        │
└─────────────────────────────┬───────────────────────────────────┘
                              │ HTTP Request
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Spring Boot Application                     │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                    Presentation Layer                      │  │
│  │              Controller, Thymeleaf Templates               │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                     Validation Layer                       │  │
│  │      DTO (@Valid), Custom Annotation (@PasswordMatching)   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                     Security Layer                         │  │
│  │     Spring Security, CustomUserDetails, Form Login         │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                   Application Layer                        │  │
│  │              Service (Use Case), Mapper                    │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                      Domain Layer                          │  │
│  │            Entity, VO (핵심 비즈니스 규칙 포함)              │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                  Infrastructure Layer                      │  │
│  │                 Repository, EmailSender                    │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────┬───────────────────────────────────┘
                              │ JDBC / SMTP
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│              Database (H2 / MySQL) + Gmail SMTP                  │
└─────────────────────────────────────────────────────────────────┘
```

### 레이어별 책임

| Layer | 책임 | 주요 컴포넌트 |
|-------|------|--------------|
| **Presentation** | HTTP 요청/응답 처리, 뷰 렌더링 | Controller, Thymeleaf |
| **Validation** | 입력값 검증 | DTO, Custom Annotation |
| **Security** | 인증/인가, 세션 관리 | Spring Security, UserDetails |
| **Application** | 유스케이스 구현, 객체 변환 | Service, Mapper |
| **Domain** | 핵심 비즈니스 규칙 | Entity, VO |
| **Infrastructure** | 데이터 영속성, 외부 서비스 연동 | Repository, JavaMailSender |

---

## 프로젝트 구조
```
src/main/java/com/mydata/mydatatestbed/
├── MydataTestbedApplication.java
│
├── config/                              # 설정
│   ├── AuditConfig.java                 # JPA Auditing 설정
│   └── SecurityConfig.java              # Spring Security 설정
│
├── controller/                          # 컨트롤러
│   ├── MainController.java
│   └── MemberController.java            # 회원가입/로그인/이메일인증
│
├── entity/                              # Entity
│   ├── BaseTimeEntity.java              # 공통 시간 필드 (createdAt, updatedAt)
│   ├── Member.java                      # 회원 Entity
│   ├── EmailVerificationToken.java      # 이메일 인증 토큰 Entity
│   └── Enum/
│       └── MemberRole.java              # 회원 권한 Enum
│
├── vo/                                  # Value Objects
│   ├── EmailVo.java                     # 이메일 VO (형식 검증 포함)
│   ├── PasswordVo.java                  # 비밀번호 VO
│   └── PhoneVo.java                     # 전화번호 VO (형식 검증 포함)
│
├── repository/                          # Repository
│   ├── MemberRepository.java
│   └── EmailVerificationTokenRepository.java
│
├── dto/                                 # DTO
│   └── member/
│       ├── MemberSignupRequestDto.java  # 회원가입 요청
│       └── MemberResponseDto.java       # 회원 응답
│
├── mapper/                              # Mapper (DTO ↔ Entity 변환)
│   └── MemberMapper.java
│
├── service/                             # Service
│   ├── MemberService.java               # 회원 서비스 인터페이스
│   ├── EmailService.java                # 이메일 서비스 인터페이스
│   └── impl/
│       ├── MemberServiceImpl.java       # 회원 서비스 구현체
│       └── EmailServiceImpl.java        # 이메일 서비스 구현체
│
├── security/                            # Spring Security
│   ├── CustomUserDetails.java           # UserDetails 구현
│   └── CustomUserDetailsService.java    # UserDetailsService 구현
│
└── validation/                          # 커스텀 Validation
    ├── PasswordMatching.java            # 비밀번호 일치 검증 어노테이션
    └── PasswordMatchingValidator.java   # 검증 로직
```

### 프론트엔드 구조
```
src/main/resources/
├── templates/
│   ├── layout/
│   │   ├── default-layout.html          # 기본 레이아웃
│   │   ├── header.html                  # 헤더
│   │   ├── footer.html                  # 푸터
│   │   └── sidebar.html                 # 사이드바
│   ├── fragments/
│   │   ├── breadcrumb.html              # 브레드크럼
│   │   ├── pagination.html              # 페이지네이션
│   │   └── page-banner.html             # 페이지 배너
│   ├── main/
│   │   └── index.html                   # 메인 페이지
│   ├── member/
│   │   ├── login.html                   # 로그인 페이지
│   │   ├── signup-step1-terms.html      # 회원가입 1단계 - 약관동의
│   │   ├── signup-step2-phone.html      # 회원가입 2단계 - 휴대폰인증
│   │   ├── signup-step3-info.html       # 회원가입 3단계 - 정보입력
│   │   ├── signup-step4-email.html      # 회원가입 4단계 - 이메일인증 대기
│   │   ├── signup-complete.html         # 회원가입 완료
│   │   ├── verify-email-success.html    # 이메일 인증 성공
│   │   └── verify-email-failed.html     # 이메일 인증 실패
│   └── error/
│
└── static/
    ├── css/
    │   ├── common.css                   # 공통 스타일
    │   ├── header.css                   # 헤더 스타일
    │   ├── footer.css                   # 푸터 스타일
    │   ├── sidebar.css                  # 사이드바 스타일
    │   ├── main.css                     # 메인 페이지 스타일
    │   ├── sub-page.css                 # 서브 페이지 스타일
    │   └── form.css                     # 폼 스타일
    └── js/
        ├── common.js                    # 공통 스크립트
        └── main.js                      # 메인 페이지 스크립트
```

---

## 메뉴 구조
```
📁 마이데이터 테스트베드
│
├── 🏠 메인
│
├── 📖 테스트베드 소개
│   ├── 마이데이터 서비스 소개
│   └── 마이데이터 테스트베드 소개
│
├── 📚 API 가이드
│   ├── API 가이드 (기본규격/인증규격/처리절차)
│   ├── 마이데이터 인증 API 규격
│   ├── 마이데이터 지원 API 규격
│   └── 마이데이터 정보제공 API 규격
│
├── 🧪 테스트베드
│   ├── 마이데이터 서비스 테스트
│   └── API 서버 테스트
│
├── ✅ 적합성 심사
│   ├── 기능적합성 심사
│   └── 보안취약점 결과 점검
│
├── 💬 고객지원
│   ├── 공지사항
│   ├── FAQ
│   ├── 문의하기
│   ├── 자료실
│   └── 자유게시판
│
└── 👤 회원
    ├── 로그인
    └── 회원가입 (4단계)
        ├── Step 1: 약관동의
        ├── Step 2: 휴대폰 인증
        ├── Step 3: 회원정보 입력
        └── Step 4: 이메일 인증
```

---

## ERD
```
┌──────────────────┐       ┌──────────────────────────┐
│     members      │       │ email_verification_tokens│
├──────────────────┤       ├──────────────────────────┤
│ id (PK)          │       │ id (PK)                  │
│ email (UK)       │◄──────│ email                    │
│ password         │       │ token (UK)               │
│ name             │       │ expires_at               │
│ phone            │       │ created_at               │
│ company          │       └──────────────────────────┘
│ department       │
│ role             │       ┌──────────────────┐
│ email_verified   │       │     notices      │
│ phone_verified   │       ├──────────────────┤
│ last_login_at    │       │ id (PK)          │
│ created_at       │       │ title            │
│ updated_at       │       │ content          │
└──────────────────┘       │ is_pinned        │
         │                 │ view_count       │
         │                 │ attachment_path  │
         │                 │ attachment_name  │
         ├────────────────►│ author_id (FK)   │
         │                 │ created_at       │
         │                 │ updated_at       │
         │                 └──────────────────┘
         │
         │                 ┌──────────────────┐
         │                 │      faqs        │
         │                 ├──────────────────┤
         │                 │ id (PK)          │
         │                 │ category         │
         │                 │ question         │
         │                 │ answer           │
         │                 │ order_num        │
         │                 │ is_active        │
         │                 │ created_at       │
         │                 │ updated_at       │
         │                 └──────────────────┘
         │
         │                 ┌──────────────────┐
         │                 │    inquiries     │
         │                 ├──────────────────┤
         │                 │ id (PK)          │
         ├────────────────►│ member_id (FK)   │
         │                 │ title            │
         │                 │ content          │
         │                 │ answer           │
         │                 │ status           │
         │                 │ answered_at      │
         ├────────────────►│ answerer_id (FK) │
         │                 │ created_at       │
         │                 │ updated_at       │
         │                 └──────────────────┘
         │
         │                 ┌──────────────────┐
         │                 │    resources     │
         │                 ├──────────────────┤
         │                 │ id (PK)          │
         │                 │ title            │
         │                 │ description      │
         │                 │ file_path        │
         │                 │ file_name        │
         │                 │ file_size        │
         │                 │ download_count   │
         ├────────────────►│ author_id (FK)   │
         │                 │ created_at       │
         │                 │ updated_at       │
         │                 └──────────────────┘
         │
         │                 ┌──────────────────┐
         │                 │      boards      │
         │                 ├──────────────────┤
         │                 │ id (PK)          │
         └────────────────►│ member_id (FK)   │
                           │ title            │
                           │ content          │
                           │ view_count       │
                           │ attachment_path  │
                           │ attachment_name  │
                           │ created_at       │
                           │ updated_at       │
                           └──────────────────┘
```

---

## 이메일 인증 플로우

회원가입 시 이메일 인증을 통해 실제 사용자임을 확인합니다. 인증 방식은 **링크 클릭 방식(Token-based)**을 사용하며, Gmail SMTP를 통해 인증 메일을 발송합니다.

### 전체 플로우
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           이메일 인증 플로우                                  │
└─────────────────────────────────────────────────────────────────────────────┘

[Step 3 완료] 회원정보 입력 후 "다음" 버튼 클릭
      │
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│  MemberController.signupStep3Process()                                       │
│    │                                                                         │
│    ├─→ MemberService.signup(requestDto)                                      │
│    │     └─→ Member 생성 (emailVerified = false)                             │
│    │     └─→ MemberRepository.save(member)                                   │
│    │                                                                         │
│    └─→ EmailService.sendVerificationEmail(email)                             │
│          ├─→ EmailVerificationToken 생성 (UUID, 24시간 유효)                  │
│          ├─→ EmailVerificationTokenRepository.save(token)                    │
│          └─→ JavaMailSender.send(message) - 인증 링크 포함                    │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      ▼
[Step 4] 이메일 인증 대기 화면 (타이머 표시, 재발송 버튼)
      │
      │  사용자가 메일함 확인 후 인증 링크 클릭
      │  URL: /member/verify-email?token=a1b2c3d4-e5f6-7890-abcd-ef1234567890
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│  MemberController.verifyEmail(token)                                         │
│    │                                                                         │
│    ├─→ EmailService.verifyToken(token)                                       │
│    │     ├─→ EmailVerificationTokenRepository.findByToken(token)             │
│    │     ├─→ token.isExpired() 확인                                          │
│    │     └─→ 검증 성공 시 email 반환                                          │
│    │                                                                         │
│    └─→ MemberService.verifyEmail(email)                                      │
│          ├─→ MemberRepository.findByEmail(email)                             │
│          └─→ member.verifyEmail() - emailVerified = true                     │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      ▼
[인증 완료] verify-email-success.html 표시
      │
      ▼
[로그인 가능] CustomUserDetails.isEnabled() → member.isEmailVerified() → true
```

### 클래스별 역할

| 계층 | 클래스 | 역할 | 주요 메서드 |
|------|--------|------|------------|
| **Entity** | `Member` | 회원 정보 저장 | `verifyEmail()` - emailVerified를 true로 변경 |
| **Entity** | `EmailVerificationToken` | 인증 토큰 저장 | `isExpired()` - 만료 여부 확인 |
| **Repository** | `MemberRepository` | 회원 DB 접근 | `findByEmail()`, `existsByEmail()` |
| **Repository** | `EmailVerificationTokenRepository` | 토큰 DB 접근 | `findByToken()`, `deleteByEmail()` |
| **Service** | `MemberService` | 회원 비즈니스 로직 | `signup()`, `verifyEmail()` |
| **Service** | `EmailService` | 이메일 발송/검증 | `sendVerificationEmail()`, `verifyToken()` |
| **Controller** | `MemberController` | HTTP 요청 처리 | `signupStep3Process()`, `verifyEmail()` |
| **Security** | `CustomUserDetails` | 로그인 시 계정 상태 확인 | `isEnabled()` → emailVerified 반환 |

### 왜 이메일 인증이 필요한가?

`CustomUserDetails.isEnabled()` 메서드가 `member.isEmailVerified()`를 반환하기 때문에, 이메일 인증을 완료하지 않으면 Spring Security가 로그인을 거부합니다.

```java
// CustomUserDetails.java
@Override
public boolean isEnabled() {
    return member.isEmailVerified();  // false면 로그인 불가
}
```

### 토큰 설계

이메일 링크에 사용자 이메일을 직접 노출하면 보안 문제가 발생할 수 있습니다. 대신 추측 불가능한 UUID 토큰을 사용합니다.

```
인증 URL 예시:
http://localhost:8080/member/verify-email?token=a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

| 필드 | 설명 |
|------|------|
| `token` | UUID 형식, 추측 불가능 |
| `email` | 토큰과 매핑된 이메일 주소 |
| `expiresAt` | 만료 시간 (기본 24시간) |
| `createdAt` | 생성 시간 |

### Gmail SMTP 설정

```yaml
# application.yml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}      # Gmail 주소
    password: ${MAIL_PASSWORD}      # Gmail 앱 비밀번호 (16자리)
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

> **Gmail 앱 비밀번호 생성 방법:**
> 1. Google 계정 → 보안 → 2단계 인증 활성화
> 2. 2단계 인증 → 앱 비밀번호 → "메일" 선택 → 생성
> 3. 16자리 비밀번호를 환경변수로 설정

### 미인증 회원 정리 (배치)

일정 기간(24시간) 동안 이메일 인증을 완료하지 않은 회원은 배치 작업으로 삭제합니다. 이를 통해 "유령 계정"이 DB에 쌓이는 것을 방지합니다.

```java
// 매일 새벽 3시 실행
@Scheduled(cron = "0 0 3 * * *")
public void cleanupUnverifiedMembers() {
    // 만료된 토큰 삭제
    tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    
    // 24시간 이상 미인증 회원 삭제
    LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
    memberRepository.deleteUnverifiedMembersCreatedBefore(cutoff);
}
```

### 코드 구현 예시

#### 1. EmailVerificationToken Entity

```java
@Entity
@Table(name = "email_verification_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public EmailVerificationToken(String email, int expirationHours) {
        this.token = UUID.randomUUID().toString();
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusHours(expirationHours);
    }

    // 토큰 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
```

#### 2. EmailVerificationTokenRepository

```java
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
```

#### 3. Member Entity (verifyEmail 메서드 추가)

```java
@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    // ... 기존 필드들 ...

    @Column(nullable = false)
    private boolean emailVerified = false;

    // 이메일 인증 완료 처리
    public void verifyEmail() {
        this.emailVerified = true;
    }
}
```

#### 4. EmailService 인터페이스

```java
public interface EmailService {

    // 인증 메일 발송, 만료 시간 반환
    LocalDateTime sendVerificationEmail(String email);

    // 토큰 검증, 성공 시 이메일 반환
    String verifyToken(String token);

    // 인증 메일 재발송
    LocalDateTime resendVerificationEmail(String email);
}
```

#### 5. EmailServiceImpl 구현체

```java
@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final int TOKEN_EXPIRATION_HOURS = 24;

    @Override
    public LocalDateTime sendVerificationEmail(String email) {
        // 토큰 생성 및 저장
        EmailVerificationToken token = EmailVerificationToken.builder()
                .email(email)
                .expirationHours(TOKEN_EXPIRATION_HOURS)
                .build();
        tokenRepository.save(token);

        // 이메일 발송
        sendEmail(email, token.getToken());

        return token.getExpiresAt();
    }

    @Override
    @Transactional(readOnly = true)
    public String verifyToken(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (verificationToken.isExpired()) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        return verificationToken.getEmail();
    }

    @Override
    public LocalDateTime resendVerificationEmail(String email) {
        // 기존 토큰 삭제
        tokenRepository.deleteByEmail(email);

        // 새 토큰으로 재발송
        return sendVerificationEmail(email);
    }

    private void sendEmail(String toEmail, String token) {
        String verificationUrl = baseUrl + "/member/verify-email?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("[마이데이터 테스트베드] 이메일 인증을 완료해주세요");
            helper.setText(buildEmailContent(verificationUrl), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    private String buildEmailContent(String verificationUrl) {
        return """
            <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                <h2 style="color: #1e3a5f;">마이데이터 테스트베드</h2>
                <p>안녕하세요, 회원가입을 완료하려면 아래 버튼을 클릭해주세요.</p>
                <a href="%s" style="display: inline-block; padding: 12px 24px; 
                   background-color: #0d6efd; color: white; text-decoration: none; 
                   border-radius: 4px; margin: 20px 0;">이메일 인증하기</a>
                <p style="color: #666; font-size: 14px;">이 링크는 24시간 동안 유효합니다.</p>
            </div>
            """.formatted(verificationUrl);
    }
}
```

#### 6. MemberService (verifyEmail 메서드 추가)

```java
public interface MemberService {
    void signup(MemberSignupRequestDto requestDto);
    boolean isEmailDuplicate(String email);
    void verifyEmail(String email);  // 추가
}
```

```java
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void verifyEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        member.verifyEmail();  // emailVerified = true
        // JPA dirty checking으로 자동 저장
    }

    // ... 기존 메서드들 ...
}
```

#### 7. MemberController (이메일 인증 관련 엔드포인트)

```java
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    // Step 3 처리: 회원 생성 + 인증 메일 발송
    @PostMapping("/signup/step3")
    public String signupStep3Process(
            @Valid @ModelAttribute MemberSignupRequestDto requestDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "member/signup-step3-info";
        }

        // 이메일 중복 체크
        if (memberService.isEmailDuplicate(requestDto.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
            return "member/signup-step3-info";
        }

        // 1. 회원 생성 (emailVerified = false)
        memberService.signup(requestDto);

        // 2. 인증 메일 발송
        LocalDateTime expiresAt = emailService.sendVerificationEmail(requestDto.getEmail());

        // 3. Step 4 페이지에 필요한 데이터 전달
        session.setAttribute("signupEmail", requestDto.getEmail());
        model.addAttribute("email", requestDto.getEmail());
        model.addAttribute("expiresAt", expiresAt);

        return "member/signup-step4-email";
    }

    // Step 4 페이지 (이메일 인증 대기)
    @GetMapping("/signup/step4")
    public String signupStep4(HttpSession session, Model model) {
        String email = (String) session.getAttribute("signupEmail");
        if (email == null) {
            return "redirect:/member/signup/step1";
        }
        model.addAttribute("email", email);
        return "member/signup-step4-email";
    }

    // 이메일 인증 처리 (사용자가 메일의 링크 클릭 시)
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        try {
            // 1. 토큰 검증 → 이메일 반환
            String email = emailService.verifyToken(token);

            // 2. 회원 이메일 인증 처리
            memberService.verifyEmail(email);

            // 3. 성공 페이지
            model.addAttribute("email", email);
            return "member/verify-email-success";

        } catch (IllegalArgumentException e) {
            // 4. 실패 페이지 (토큰 만료 또는 유효하지 않음)
            model.addAttribute("error", e.getMessage());
            return "member/verify-email-failed";
        }
    }

    // 인증 메일 재발송
    @PostMapping("/resend-verification")
    @ResponseBody
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        try {
            LocalDateTime expiresAt = emailService.resendVerificationEmail(email);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "expiresAt", expiresAt.toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
```

> **핵심 포인트:** `memberService.verifyEmail(email)`이 항상 동작하는 이유는 Step 3에서 이미 Member를 DB에 저장했기 때문입니다. 이메일 인증 링크 클릭 시점에는 해당 이메일로 가입된 Member가 이미 존재합니다.

---

## 실행 방법

### 요구사항
- Java 21+
- Gradle 8.x
- Gmail 계정 (이메일 발송용, 앱 비밀번호 필요)

### 개발 환경 실행
```bash
# 1. 프로젝트 클론
git clone https://github.com/YangGyunShin/mydata-testbed.git
cd mydata-testbed

# 2. 환경변수 설정 (이메일 발송용)
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-16-digit-app-password

# 3. 애플리케이션 실행
./gradlew bootRun

# 4. 브라우저에서 접속
http://localhost:8080
```

### IntelliJ에서 환경변수 설정
Run → Edit Configurations → Environment variables에 추가:
```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=abcd efgh ijkl mnop
```

### H2 콘솔 접속 (개발용)
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (비워두기)
```

---

## 개발 로드맵

### Phase 1: 기본 구조 ✅ 완료
- [x] 프로젝트 생성 및 의존성 설정
- [x] 공통 레이아웃 (Header, Footer, Sidebar)
- [x] CSS (common, header, footer, sidebar, main, sub-page, form)
- [x] JS (common.js, main.js)
- [x] 메인 페이지 템플릿 (index.html)
- [x] Config 설정 (SecurityConfig, AuditConfig)
- [x] MainController

### Phase 2: 회원 기능 ✅ 완료
- [x] VO (EmailVo, PasswordVo, PhoneVo) - 도메인 규칙 검증 포함
- [x] Entity (BaseTimeEntity, Member)
- [x] Enum (MemberRole)
- [x] Repository (MemberRepository)
- [x] DTO (MemberSignupRequestDto, MemberResponseDto)
- [x] Custom Validation (@PasswordMatching)
- [x] Mapper (MemberMapper)
- [x] Service (MemberService, MemberServiceImpl)
- [x] Security (CustomUserDetails, CustomUserDetailsService)
- [x] SecurityConfig 수정 (PasswordEncoder, UserDetailsService 연동)
- [x] MemberController (회원가입/로그인 페이지)
- [x] 회원가입 템플릿 (step1~4)
- [x] 로그인 템플릿
- [x] 이메일 인증 기능 (Entity, Repository, Service, Controller, 템플릿)

### Phase 3: 게시판 기능 (진행 예정)
- [ ] Entity (Notice, Faq, Inquiry, Resource, Board)
- [ ] Enum (FaqCategory, InquiryStatus)
- [ ] Repository
- [ ] DTO, Mapper, Service
- [ ] Controller
- [ ] 템플릿 (목록, 상세, 작성 페이지)

### Phase 4: 핵심 기능
- [ ] API 가이드 페이지
- [ ] 테스트베드 기능
- [ ] 적합성 심사 기능

### Phase 5: 완성도 높이기
- [ ] 검색 기능
- [ ] 페이징
- [ ] 파일 첨부/다운로드
- [ ] 반응형 디자인 점검
- [ ] 에러 페이지 (404, 500)
- [ ] 미인증 회원 정리 배치 작업

---

## 주요 설계 결정 사항

### 1. VO에 검증 로직을 넣는 이유
VO의 validate 메서드는 "값이 무엇인지"를 정의하는 도메인 규칙입니다. 어떤 애플리케이션에서 사용하든 이메일 형식은 동일하게 검증되어야 합니다. VO에서 검증하면 **항상 유효한 도메인 모델(Always Valid Domain Model)** 을 보장할 수 있습니다.

### 2. Entity에 비즈니스 메서드를 넣는 이유
`member.verifyEmail()` 같은 메서드는 "회원의 이메일이 인증되었음을 표시한다"는 도메인 규칙입니다. 이를 Service에서 직접 필드를 조작하면 Setter가 필요해지고 캡슐화가 깨집니다. Entity가 자신의 상태를 스스로 관리하도록 합니다.

### 3. 커스텀 Validation 어노테이션을 만드는 이유
비밀번호 일치 검증은 회원가입뿐 아니라 비밀번호 변경, 재설정 등에서도 재사용됩니다. `@PasswordMatching` 어노테이션으로 만들면 `@Valid` 하나로 모든 검증이 자동 실행되고, Service가 깔끔해집니다.

### 4. Form Login을 사용하는 이유
Spring Security의 Form Login을 사용하면 로그인/로그아웃 로직을 직접 구현할 필요가 없습니다. `CustomUserDetailsService`만 구현하면 Spring Security가 나머지(비밀번호 비교, 세션 관리 등)를 자동 처리합니다.

### 5. 이메일 인증에서 "먼저 회원 생성" 방식을 선택한 이유
회원을 먼저 생성하고(emailVerified=false) 이메일 인증 후 활성화하는 방식이 구현이 더 단순합니다. 인증 링크에 토큰만 있으면 되고, 세션 만료 걱정이 없습니다. 미인증 회원은 배치 작업으로 정리합니다.

### 6. RequestDto에만 @Setter를 허용하는 이유
Spring MVC의 폼 바인딩은 기본 생성자로 객체를 만들고 Setter로 값을 주입합니다. 따라서 RequestDto에는 `@Setter`가 필요합니다. 하지만 Entity와 ResponseDto는 Builder 패턴만 사용하여 불변성을 유지합니다.

---

## 라이선스

This project is for educational purposes only.

---

## 참고 자료

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Mail Documentation](https://docs.spring.io/spring-framework/reference/integration/email.html)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [금융분야 마이데이터 테스트베드](https://developers.mydatakorea.org/mdtb/)
