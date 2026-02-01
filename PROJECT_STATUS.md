# 📊 프로젝트 진행 상황

> **마지막 업데이트**: 2026-02-01  
> **현재 Phase**: Phase 4-1 진행 중 (데이터 표준 API 완료, 마이데이터 인증 API 규격 예정)

---

## 🎯 전체 진행률

```
Phase 1: 기본 구조      [██████████] 100% ✅
Phase 2: 회원 기능      [██████████] 100% ✅
Phase 3: 게시판 기능    [██████████] 100% ✅
Phase 4: 핵심 기능      [██░░░░░░░░]  20% 🔄
Phase 5: 완성도         [░░░░░░░░░░]   0% ⏳
```

---

## ✅ 완료된 작업

### Phase 1: 기본 구조 ✅

| 분류 | 항목 | 파일 |
|------|------|------|
| **Config** | 보안, 웹, Auditing 설정 | `SecurityConfig`, `WebConfig`, `AuditConfig` |
| **Layout** | 레이아웃 템플릿 | `default-layout`, `header`, `footer`, `sidebar` |
| **Fragments** | 공통 UI 조각 | `breadcrumb`, `pagination`, `page-banner` |
| **CSS** | 스타일시트 | `common`, `header`, `footer`, `sidebar`, `main`, `sub-page`, `form` |
| **JS** | 스크립트 | `common.js`, `main.js` |
| **Page** | 메인 페이지 | `index.html`, `MainController` |

### Phase 2: 회원 기능 ✅

| 분류 | 항목 | 파일 |
|------|------|------|
| **Entity** | 회원, 공통 시간 | `Member`, `BaseTimeEntity` |
| **VO** | 값 객체 | `Email`, `Password`, `Phone` |
| **Enum** | 회원 역할 | `MemberRole` |
| **Repository** | 데이터 접근 | `MemberRepository`, `EmailVerificationTokenRepository` |
| **DTO** | 데이터 전송 | `MemberSignupRequestDto`, `MemberResponseDto` |
| **Mapper** | 변환 | `MemberMapper` |
| **Service** | 비즈니스 로직 | `MemberService`, `EmailService` |
| **Security** | 인증/인가 | `CustomUserDetails`, `CustomUserDetailsService` |
| **Controller** | 요청 처리 | `MemberController` |
| **Templates** | 화면 | `login`, `signup-step1~4`, `verify-email-*` |

### Phase 3: 게시판 기능 ✅

| 기능 | 상태 | 주요 파일 |
|------|------|----------|
| 3-1. 공지사항 (Notice) | ✅ | `Notice`, `NoticeRepository`, `NoticeService` |
| 3-2. FAQ | ✅ | `Faq`, `FaqCategory`, `FaqRepository`, `FaqService` |
| 3-3. 문의하기 (Inquiry) | ✅ | `Inquiry`, `InquiryStatus`, `InquiryRepository`, `InquiryService` |
| 3-4. 자료실 (Resource) | ✅ | `Resource`, `ResourceRepository`, `ResourceService` |
| 3-5. 자유게시판 (Board) | ✅ | `Board`, `BoardRepository`, `BoardService`, `FileService` |

### Phase 4-1: API 가이드 - 데이터 표준 API ✅

| 항목 | 파일 | URL |
|------|------|-----|
| **Controller** | `ApiGuideController.java` | `/api-guide/**` |
| **CSS** | `api-guide.css` | - |
| **기본규격** | `basic-spec.html` | `/api-guide/base` |
| **인증규격** | `auth-spec.html` | `/api-guide/auth` |
| **참여자별 API 처리 절차** | `process-spec.html` | `/api-guide/process` |

**ApiGuideController 사이드바 구조:**
```
API가이드 (사이드바 제목)
├── 데이터 표준 API 기본규격     → /api-guide/base     ✅
├── 데이터 표준 API 인증규격     → /api-guide/auth     ✅
├── 마이데이터 참여자별 API 처리 절차 → /api-guide/process ✅
├── 마이데이터 인증 API 규격     → /cert-api           ⬜ (별도 컨트롤러)
├── 마이데이터 지원 API 규격     → /support-api        ⬜ (별도 컨트롤러)
└── 마이데이터 정보제공 API 규격 → /info-api           ⬜ (별도 컨트롤러)
```

---

## 📝 남은 작업

### Phase 4-1 계속: API 가이드 - 나머지 섹션 ⬜

> **중요**: 마이데이터 인증/지원/정보제공 API 규격은 각각 **별도 컨트롤러 + 자체 사이드바**를 가진 독립 섹션

| 항목 | 컨트롤러 | URL 패턴 | 사이드바 메뉴 | 상태 |
|------|---------|----------|-------------|------|
| 마이데이터 인증 API 규격 | `CertApiController` | `/cert-api/**` | 개별인증 API, 통합인증 API | ⬜ 다음 작업 |
| 마이데이터 지원 API 규격 | `SupportApiController` | `/support-api/**` | (스캔 필요) | ⬜ |
| 마이데이터 정보제공 API 규격 | `InfoApiController` | `/info-api/**` | (스캔 필요) | ⬜ |

### Phase 4-2: 테스트베드 기능 ⬜

| URL | 설명 |
|-----|------|
| `/testbed/service` | 마이데이터 서비스 테스트 |
| `/testbed/api` | API 서버 테스트 |

### Phase 4-3: 적합성 심사 ⬜

| URL | 설명 |
|-----|------|
| `/conformance/functional` | 기능적합성 심사 |
| `/conformance/security` | 보안취약점 결과 점검 |

### Phase 5: 완성도 ⬜

| 항목 | 상태 |
|------|------|
| 검색 기능 고도화 | ⬜ |
| 반응형 디자인 개선 | ⬜ |
| 에러 페이지 (404, 500) | ⬜ |
| 전역 예외 처리 | ⬜ |

---

## 📁 현재 파일 구조

```
src/main/java/com/mydata/mydatatestbed/
├── MydataTestbedApplication.java
│
├── config/
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── AuditConfig.java
│
├── controller/
│   ├── MainController.java
│   ├── MemberController.java
│   ├── SupportController.java
│   └── ApiGuideController.java          # ✅ Phase 4-1 추가
│
├── entity/
│   ├── BaseTimeEntity.java
│   ├── Member.java
│   ├── Notice.java
│   ├── Faq.java
│   ├── Inquiry.java
│   ├── Resource.java
│   ├── Board.java
│   └── Enum/
│       ├── MemberRole.java
│       ├── FaqCategory.java
│       └── InquiryStatus.java
│
├── vo/
│   ├── EmailVo.java
│   ├── PasswordVo.java
│   └── PhoneVo.java
│
├── repository/
│   ├── MemberRepository.java
│   ├── EmailVerificationTokenRepository.java
│   ├── NoticeRepository.java
│   ├── FaqRepository.java
│   ├── InquiryRepository.java
│   ├── ResourceRepository.java
│   └── BoardRepository.java
│
├── dto/
│   ├── member/
│   ├── notice/
│   ├── faq/
│   ├── inquiry/
│   ├── resource/
│   └── board/
│
├── mapper/
│   ├── MemberMapper.java
│   ├── NoticeMapper.java
│   ├── FaqMapper.java
│   ├── InquiryMapper.java
│   ├── ResourceMapper.java
│   └── BoardMapper.java
│
├── service/
│   ├── impl/ (각 ServiceImpl 포함)
│   └── ... (각 Service 인터페이스)
│
├── util/
│   └── FileSizeFormatter.java
│
├── security/
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
│
└── validation/
    ├── PasswordMatching.java
    └── PasswordMatchingValidator.java

src/main/resources/
├── application.yml
├── data.sql
├── templates/
│   ├── layout/
│   │   ├── default-layout.html
│   │   ├── header.html
│   │   ├── footer.html
│   │   └── sidebar.html
│   ├── fragments/
│   │   ├── breadcrumb.html
│   │   ├── pagination.html
│   │   └── page-banner.html
│   ├── main/
│   │   └── index.html
│   ├── member/
│   │   ├── login.html
│   │   ├── signup-step1-terms.html ~ step4-email.html
│   │   ├── verify-email-success.html
│   │   └── verify-email-failed.html
│   ├── support/
│   │   ├── notice-list.html, notice-detail.html
│   │   ├── faq.html
│   │   ├── inquiry-form.html, inquiry-list.html, inquiry-detail.html
│   │   ├── resource-list.html, resource-detail.html
│   │   ├── board-list.html, board-detail.html, board-write.html
│   ├── api-guide/                        # ✅ Phase 4-1 추가
│   │   ├── basic-spec.html              # 데이터 표준 API 기본규격
│   │   ├── auth-spec.html               # 데이터 표준 API 인증규격
│   │   └── process-spec.html            # 마이데이터 참여자별 API 처리 절차
│   └── error/
└── static/
    ├── css/
    │   ├── common.css, header.css, footer.css
    │   ├── sidebar.css, main.css, sub-page.css, form.css
    │   └── api-guide.css                 # ✅ Phase 4-1 추가
    └── js/
        ├── common.js
        └── main.js
```

---

## 🔗 구현된 URL 매핑

### 공개 URL (인증 불필요)

| URL | Method | 설명 |
|-----|--------|------|
| `/` | GET | 메인 페이지 |
| `/member/login` | GET/POST | 로그인 |
| `/member/signup/step1~4` | GET/POST | 회원가입 |
| `/member/verify-email` | GET | 이메일 인증 |
| `/member/resend-verification` | POST | 인증 메일 재발송 |
| `/support/notice` | GET | 공지사항 목록 |
| `/support/notice/{id}` | GET | 공지사항 상세 |
| `/support/faq` | GET | FAQ |
| `/support/resource` | GET | 자료실 목록 |
| `/support/resource/{id}` | GET | 자료실 상세 |
| `/support/resource/{id}/download` | GET | 자료 다운로드 |
| `/support/board` | GET | 자유게시판 목록 |
| `/support/board/{id}` | GET | 자유게시판 상세 |
| `/support/board/{id}/download` | GET | 첨부파일 다운로드 |
| `/api-guide` | GET | → `/api-guide/base` 리다이렉트 |
| `/api-guide/base` | GET | 데이터 표준 API 기본규격 ✅ |
| `/api-guide/auth` | GET | 데이터 표준 API 인증규격 ✅ |
| `/api-guide/process` | GET | 참여자별 API 처리 절차 ✅ |

### 인증 필요 URL

| URL | Method | 설명 |
|-----|--------|------|
| `/support/inquiry` | GET/POST | 문의 작성 폼/등록 |
| `/support/inquiry/list` | GET | 내 문의 목록 |
| `/support/inquiry/{id}` | GET | 문의 상세 |
| `/support/board/write` | GET/POST | 게시글 작성 |
| `/support/board/{id}/edit` | GET/POST | 게시글 수정 |
| `/support/board/{id}/delete` | POST | 게시글 삭제 |

---

## 📚 관련 문서

| 문서 | 설명 |
|------|------|
| [README.md](README.md) | 프로젝트 소개, 빠른 시작 |
| [API_SPEC.md](API_SPEC.md) | API 엔드포인트 상세 명세 |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | 트러블슈팅 가이드 |
| [NEXT_SESSION_TEMPLATE.md](NEXT_SESSION_TEMPLATE.md) | 코딩 컨벤션, 다음 작업 |
