# 📚 API 명세서

마이데이터 테스트베드 프로젝트의 URL 엔드포인트 명세입니다.

---

## 목차

- [회원 (Member)](#회원-member)
- [고객지원 - 공지사항 (Notice)](#고객지원---공지사항-notice)
- [고객지원 - FAQ](#고객지원---faq-예정)
- [고객지원 - 문의하기 (Inquiry)](#고객지원---문의하기-inquiry-예정)
- [고객지원 - 자료실 (Resource)](#고객지원---자료실-resource-예정)
- [고객지원 - 자유게시판 (Board)](#고객지원---자유게시판-board-예정)
- [메인](#메인)

---

## 회원 (Member)

### 로그인

| 항목 | 내용 |
|------|------|
| **URL** | `/member/login` |
| **Method** | `GET` / `POST` |
| **인증** | 불필요 |
| **설명** | 로그인 페이지 및 로그인 처리 (Spring Security Form Login) |

**GET 요청**: 로그인 페이지 반환

**POST 요청** (Spring Security 자동 처리):
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `email` | String | ✅ | 사용자 이메일 |
| `password` | String | ✅ | 비밀번호 |

**응답**:
- 성공: `/` (메인 페이지)로 리다이렉트
- 실패: `/member/login?error=true`로 리다이렉트

---

### 로그아웃

| 항목 | 내용 |
|------|------|
| **URL** | `/member/logout` |
| **Method** | `POST` |
| **인증** | 필요 |
| **설명** | 로그아웃 처리 (Spring Security 자동 처리) |

**응답**: `/` (메인 페이지)로 리다이렉트

---

### 회원가입 Step 1 - 약관동의

| 항목 | 내용 |
|------|------|
| **URL** | `/member/signup/step1` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 회원가입 1단계 약관동의 페이지 |

---

### 회원가입 Step 2 - 휴대폰 인증

| 항목 | 내용 |
|------|------|
| **URL** | `/member/signup/step2` |
| **Method** | `GET` / `POST` |
| **인증** | 불필요 |
| **설명** | 회원가입 2단계 휴대폰 인증 페이지 |

**POST 요청 파라미터**:
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `termsAgreed` | boolean | ✅ | 이용약관 동의 |
| `privacyAgreed` | boolean | ✅ | 개인정보처리방침 동의 |

---

### 회원가입 Step 3 - 회원정보 입력

| 항목 | 내용 |
|------|------|
| **URL** | `/member/signup/step3` |
| **Method** | `GET` / `POST` |
| **인증** | 불필요 |
| **설명** | 회원가입 3단계 회원정보 입력 및 회원 생성 |

**POST 요청 파라미터** (`MemberSignupRequestDto`):
| 파라미터 | 타입 | 필수 | 검증 규칙 | 설명 |
|----------|------|------|-----------|------|
| `email` | String | ✅ | 이메일 형식, 중복 불가 | 사용자 이메일 |
| `password` | String | ✅ | 8~20자, 영문+숫자+특수문자 | 비밀번호 |
| `passwordConfirm` | String | ✅ | password와 일치 | 비밀번호 확인 |
| `name` | String | ✅ | 2~50자 | 이름 |
| `phone` | String | ✅ | 010-XXXX-XXXX 형식 | 휴대폰 번호 |
| `company` | String | ❌ | 최대 100자 | 소속 회사/기관 |
| `department` | String | ❌ | 최대 50자 | 부서 |

**처리 로직**:
1. 회원 생성 (`emailVerified = false`)
2. 이메일 인증 메일 발송
3. Step 4 페이지로 이동

---

### 회원가입 Step 4 - 이메일 인증 대기

| 항목 | 내용 |
|------|------|
| **URL** | `/member/signup/step4` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 이메일 인증 대기 안내 페이지 |

---

### 이메일 인증 처리

| 항목 | 내용 |
|------|------|
| **URL** | `/member/verify-email` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 이메일 인증 링크 처리 |

**Query 파라미터**:
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `token` | String | ✅ | 이메일 인증 토큰 (UUID) |

**응답**:
- 성공: `member/verify-email-success` 페이지
- 실패 (토큰 만료/유효하지 않음): `member/verify-email-failed` 페이지

---

### 인증 메일 재발송

| 항목 | 내용 |
|------|------|
| **URL** | `/member/resend-verification` |
| **Method** | `POST` |
| **인증** | 불필요 |
| **Content-Type** | `application/x-www-form-urlencoded` |
| **설명** | 이메일 인증 메일 재발송 |

**요청 파라미터**:
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `email` | String | ✅ | 재발송 대상 이메일 |

**응답** (JSON):
```json
// 성공
{
  "success": true,
  "expiresAt": "2025-01-23T12:00:00"
}

// 실패
{
  "success": false,
  "message": "에러 메시지"
}
```

---

## 고객지원 - 공지사항 (Notice)

### 공지사항 목록 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/notice` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 공지사항 목록 페이지 (페이징, 검색 지원) |

**Query 파라미터**:
| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|----------|------|------|--------|------|
| `page` | int | ❌ | 0 | 페이지 번호 (0부터 시작) |
| `keyword` | String | ❌ | "" | 검색어 (제목/내용 검색) |

**Model 데이터**:
| 속성 | 타입 | 설명 |
|------|------|------|
| `notices` | `Page<NoticeListResponseDto>` | 공지사항 목록 (페이징) |
| `keyword` | String | 검색어 |
| `breadcrumbItems` | List | 브레드크럼 데이터 |
| `sidebarMenus` | List | 사이드바 메뉴 |
| `currentMenu` | String | 현재 메뉴 URL |

**NoticeListResponseDto**:
| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long | 공지사항 ID |
| `title` | String | 제목 |
| `pinned` | boolean | 중요 공지 여부 |
| `viewCount` | int | 조회수 |
| `hasAttachment` | boolean | 첨부파일 유무 |
| `authorName` | String | 작성자 이름 |
| `createdAt` | LocalDateTime | 등록일 |

---

### 공지사항 상세 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/notice/{id}` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 공지사항 상세 페이지 (조회 시 조회수 증가) |

**Path 파라미터**:
| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `id` | Long | 공지사항 ID |

**Model 데이터**:
| 속성 | 타입 | 설명 |
|------|------|------|
| `notice` | `NoticeDetailResponseDto` | 공지사항 상세 정보 |
| `breadcrumbItems` | List | 브레드크럼 데이터 |
| `sidebarMenus` | List | 사이드바 메뉴 |
| `currentMenu` | String | 현재 메뉴 URL |

**NoticeDetailResponseDto**:
| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | Long | 공지사항 ID |
| `title` | String | 제목 |
| `content` | String | 본문 (HTML) |
| `pinned` | boolean | 중요 공지 여부 |
| `viewCount` | int | 조회수 |
| `attachmentPath` | String | 첨부파일 경로 |
| `attachmentName` | String | 첨부파일 원본명 |
| `authorName` | String | 작성자 이름 |
| `createdAt` | LocalDateTime | 등록일 |
| `updatedAt` | LocalDateTime | 수정일 |

---

## 고객지원 - FAQ (예정)

### FAQ 목록 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/faq` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | FAQ 목록 페이지 (카테고리별 필터링) |

**Query 파라미터**:
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `category` | FaqCategory | ❌ | 카테고리 필터 |

**FaqCategory Enum**:
| 값 | 설명 |
|------|------|
| `GENERAL` | 일반 |
| `SIGNUP` | 회원가입 |
| `API` | API |
| `TEST` | 테스트 |
| `CONFORMANCE` | 적합성심사 |

---

## 고객지원 - 문의하기 (Inquiry) (예정)

### 문의 작성 폼

| 항목 | 내용 |
|------|------|
| **URL** | `/support/inquiry` |
| **Method** | `GET` |
| **인증** | 필요 |
| **설명** | 문의 작성 페이지 |

---

### 문의 등록

| 항목 | 내용 |
|------|------|
| **URL** | `/support/inquiry` |
| **Method** | `POST` |
| **인증** | 필요 |
| **설명** | 문의 등록 처리 |

**요청 파라미터** (`InquiryRequestDto`):
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `title` | String | ✅ | 문의 제목 |
| `content` | String | ✅ | 문의 내용 |

---

### 내 문의 목록

| 항목 | 내용 |
|------|------|
| **URL** | `/support/inquiry/list` |
| **Method** | `GET` |
| **인증** | 필요 |
| **설명** | 로그인한 사용자의 문의 목록 |

---

## 고객지원 - 자료실 (Resource) (예정)

### 자료 목록 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/resource` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 자료실 목록 페이지 |

---

### 자료 다운로드

| 항목 | 내용 |
|------|------|
| **URL** | `/support/resource/{id}/download` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 자료 파일 다운로드 (다운로드 수 증가) |

---

## 고객지원 - 자유게시판 (Board) (예정)

### 게시글 목록 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/board` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 자유게시판 목록 페이지 |

---

### 게시글 상세 조회

| 항목 | 내용 |
|------|------|
| **URL** | `/support/board/{id}` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 게시글 상세 페이지 |

---

### 게시글 작성 폼

| 항목 | 내용 |
|------|------|
| **URL** | `/support/board/write` |
| **Method** | `GET` |
| **인증** | 필요 |
| **설명** | 게시글 작성 페이지 |

---

### 게시글 등록

| 항목 | 내용 |
|------|------|
| **URL** | `/support/board/write` |
| **Method** | `POST` |
| **인증** | 필요 |
| **설명** | 게시글 등록 처리 |

---

## 메인

### 메인 페이지

| 항목 | 내용 |
|------|------|
| **URL** | `/` |
| **Method** | `GET` |
| **인증** | 불필요 |
| **설명** | 메인 페이지 (최신 공지사항 3개 표시) |

**Model 데이터**:
| 속성 | 타입 | 설명 |
|------|------|------|
| `notices` | `List<NoticeListResponseDto>` | 최신 공지사항 3개 |

---

## 인증 필요 여부 요약

| URL 패턴 | 인증 필요 |
|----------|----------|
| `/css/**`, `/js/**`, `/images/**` | ❌ |
| `/`, `/intro/**`, `/api-guide/**` | ❌ |
| `/member/login`, `/member/signup/**` | ❌ |
| `/member/verify-email`, `/member/resend-verification` | ❌ |
| `/support/notice/**`, `/support/faq` | ❌ |
| `/support/resource/**`, `/support/board` (목록/상세) | ❌ |
| `/support/inquiry/**` | ✅ |
| `/support/board/write` | ✅ |
| `/testbed/**`, `/conformance/**` | ✅ |
| `/admin/**` | ✅ (ADMIN 권한) |
