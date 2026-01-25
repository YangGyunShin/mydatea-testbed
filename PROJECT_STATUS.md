# 📊 프로젝트 진행 상황

> **마지막 업데이트**: 2025-01-25  
> **현재 Phase**: Phase 3 진행 중 (게시판 기능)

---

## 🎯 전체 진행률

```
Phase 1: 기본 구조      [██████████] 100% ✅
Phase 2: 회원 기능      [██████████] 100% ✅
Phase 3: 게시판 기능    [██████░░░░]  60% 🔄
Phase 4: 핵심 기능      [░░░░░░░░░░]   0% ⏳
Phase 5: 완성도         [░░░░░░░░░░]   0% ⏳
```

---

## ✅ 완료된 작업

### Phase 1: 기본 구조 ✅

| 분류 | 항목 | 상태 |
|------|------|------|
| **Config** | SecurityConfig, WebConfig, AuditConfig | ✅ |
| **Layout** | default-layout, header, footer, sidebar | ✅ |
| **Fragments** | breadcrumb, pagination, page-banner | ✅ |
| **CSS** | common, header, footer, sidebar, main, sub-page, form | ✅ |
| **JS** | common.js, main.js | ✅ |
| **Controller** | MainController | ✅ |
| **Page** | index.html (메인 페이지) | ✅ |

### Phase 2: 회원 기능 ✅

| 분류 | 항목 | 상태 |
|------|------|------|
| **Entity** | Member, BaseTimeEntity | ✅ |
| **VO** | Email, Password, Phone | ✅ |
| **Enum** | MemberRole | ✅ |
| **Repository** | MemberRepository, EmailVerificationTokenRepository | ✅ |
| **DTO** | MemberSignupRequestDto, MemberResponseDto | ✅ |
| **Mapper** | MemberMapper | ✅ |
| **Service** | MemberService, EmailService | ✅ |
| **Security** | CustomUserDetails, CustomUserDetailsService | ✅ |
| **Controller** | MemberController | ✅ |
| **Templates** | login, signup-step1~4, verify-email-success/failed | ✅ |

### Phase 3: 게시판 기능 (진행 중)

#### 3-1. 공지사항 (Notice) ✅

| 항목 | 상태 |
|------|------|
| Notice Entity | ✅ |
| NoticeRepository | ✅ |
| NoticeListResponseDto, NoticeDetailResponseDto | ✅ |
| NoticeMapper | ✅ |
| NoticeService / NoticeServiceImpl | ✅ |
| SupportController (Notice 부분) | ✅ |
| notice-list.html, notice-detail.html | ✅ |

#### 3-2. FAQ ✅

| 항목 | 상태 |
|------|------|
| Faq Entity | ✅ |
| FaqCategory Enum | ✅ |
| FaqRepository | ✅ |
| FaqResponseDto | ✅ |
| FaqMapper | ✅ |
| FaqService / FaqServiceImpl | ✅ |
| SupportController (FAQ 부분) | ✅ |
| faq.html (카테고리 탭, 아코디언) | ✅ |
| data.sql 초기 데이터 | ✅ |

#### 3-3. 문의하기 (Inquiry) ✅

| 항목 | 상태 |
|------|------|
| Inquiry Entity | ✅ |
| InquiryStatus Enum (WAITING, COMPLETED) | ✅ |
| InquiryRepository | ✅ |
| InquiryRequestDto, InquiryResponseDto, InquiryListResponseDto | ✅ |
| InquiryMapper | ✅ |
| InquiryService / InquiryServiceImpl | ✅ |
| SupportController (Inquiry 부분) | ✅ |
| inquiry-form.html, inquiry-list.html, inquiry-detail.html | ✅ |

---

## 📝 남은 작업

### Phase 3-4: 자료실 (Resource)

| 항목 | 상태 |
|------|------|
| Resource Entity | ⬜ |
| ResourceRepository | ⬜ |
| ResourceListResponseDto | ⬜ |
| ResourceMapper | ⬜ |
| ResourceService / ResourceServiceImpl | ⬜ |
| SupportController (Resource 부분) | ⬜ |
| resource-list.html | ⬜ |
| 파일 다운로드 기능 | ⬜ |

### Phase 3-5: 자유게시판 (Board)

| 항목 | 상태 |
|------|------|
| Board Entity | ⬜ |
| BoardRepository | ⬜ |
| BoardRequestDto, BoardListResponseDto, BoardDetailResponseDto | ⬜ |
| BoardMapper | ⬜ |
| BoardService / BoardServiceImpl | ⬜ |
| SupportController (Board 부분) | ⬜ |
| board-list.html, board-detail.html, board-write.html | ⬜ |

### Phase 4: 핵심 기능 (예정)

| 항목 | 상태 |
|------|------|
| API 가이드 페이지 | ⬜ |
| 테스트베드 기능 | ⬜ |
| 적합성 심사 기능 | ⬜ |

### Phase 5: 완성도 (예정)

| 항목 | 상태 |
|------|------|
| 검색 기능 고도화 | ⬜ |
| 파일 첨부/다운로드 | ⬜ |
| 반응형 디자인 개선 | ⬜ |
| 에러 페이지 (404, 500) | ⬜ |
| 전역 예외 처리 | ⬜ |

---

## 📁 현재 파일 구조

```
src/main/java/com/mydata/mydatatestbed/
├── config/
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── AuditConfig.java
├── controller/
│   ├── MainController.java
│   ├── MemberController.java
│   └── SupportController.java
├── entity/
│   ├── BaseTimeEntity.java
│   ├── Member.java
│   ├── Notice.java
│   ├── Faq.java
│   ├── Inquiry.java
│   ├── Enum/
│   │   ├── MemberRole.java
│   │   ├── FaqCategory.java
│   │   └── InquiryStatus.java
│   └── vo/
│       ├── Email.java
│       ├── Password.java
│       └── Phone.java
├── repository/
│   ├── MemberRepository.java
│   ├── EmailVerificationTokenRepository.java
│   ├── NoticeRepository.java
│   ├── FaqRepository.java
│   └── InquiryRepository.java
├── dto/
│   ├── member/
│   ├── notice/
│   ├── faq/
│   └── inquiry/
├── mapper/
│   ├── MemberMapper.java
│   ├── NoticeMapper.java
│   ├── FaqMapper.java
│   └── InquiryMapper.java
├── service/
│   ├── MemberService.java
│   ├── EmailService.java
│   ├── NoticeService.java
│   ├── FaqService.java
│   └── InquiryService.java
├── service/impl/
│   ├── NoticeServiceImpl.java
│   ├── FaqServiceImpl.java
│   └── InquiryServiceImpl.java
└── security/
    ├── CustomUserDetails.java
    └── CustomUserDetailsService.java

src/main/resources/
├── templates/
│   ├── layout/
│   ├── fragments/
│   ├── main/
│   ├── member/
│   └── support/
│       ├── notice-list.html
│       ├── notice-detail.html
│       ├── faq.html
│       ├── inquiry-form.html
│       ├── inquiry-list.html
│       └── inquiry-detail.html
├── static/
│   ├── css/
│   └── js/
├── application.yml
└── data.sql
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
| `/support/notice` | GET | 공지사항 목록 |
| `/support/notice/{id}` | GET | 공지사항 상세 |
| `/support/faq` | GET | FAQ |

### 인증 필요 URL

| URL | Method | 설명 |
|-----|--------|------|
| `/support/inquiry` | GET | 문의 작성 폼 |
| `/support/inquiry` | POST | 문의 등록 |
| `/support/inquiry/list` | GET | 내 문의 목록 |
| `/support/inquiry/{id}` | GET | 문의 상세 |

---

## 📐 코딩 컨벤션

### 어노테이션 패턴

| 클래스 유형 | 어노테이션 |
|------------|-----------|
| **Entity** | `@Getter @NoArgsConstructor(access = PROTECTED)` + 생성자에 `@Builder` |
| **ResponseDto** | `@Getter @Builder` |
| **RequestDto** | `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor` |
| **Mapper** | `@Component` |
| **Service** | `@Service @RequiredArgsConstructor @Transactional(readOnly = true)` |

### 필수 규칙

| 규칙 | 설명 |
|------|------|
| ❌ No Factory Method | `of()`, `from()` 정적 팩토리 메서드 사용 금지 |
| ❌ No Setter | Entity, ResponseDto에 Setter 금지 (RequestDto만 예외) |
| ✅ Use Mapper | DTO ↔ Entity 변환은 별도 Mapper 클래스 사용 |
| ✅ LAZY Loading | `@ManyToOne`에 `fetch = FetchType.LAZY` 필수 |
| ✅ N+1 방지 | JOIN FETCH 쿼리 사용 |

---

## 📚 참고 문서

| 문서 | 설명 |
|------|------|
| `README.md` | 프로젝트 소개, 실행 방법 |
| `API_SPEC.md` | API 엔드포인트 상세 명세 |
| `TROUBLESHOOTING.md` | 트러블슈팅 가이드 |
| `NEXT_SESSION_TEMPLATE.md` | 다음 세션 작업 요청서 |
