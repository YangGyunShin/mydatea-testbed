# 📋 다음 세션 작업 가이드

> **마지막 업데이트**: 2026-02-01  
> **다음 작업**: Phase 4-1 계속 - 마이데이터 인증 API 규격 (별도 섹션)

---

## 📁 프로젝트 정보

| 항목 | 내용 |
|------|------|
| **프로젝트명** | 금융분야 마이데이터 테스트베드 클론 |
| **프로젝트 경로** | `~/Library/Mobile Documents/com~apple~CloudDocs/Spring/study/mydata-testbed` |
| **기술 스택** | Spring Boot 3.4.1, Java 21, Thymeleaf, Spring Security 6.x, JPA, H2 |

---

## ✅ 완료된 작업 요약

| Phase | 상태 | 비고 |
|-------|------|------|
| Phase 1: 기본 구조 | ✅ | 레이아웃, CSS, Security |
| Phase 2: 회원 기능 | ✅ | 4단계 회원가입, 이메일 인증 |
| Phase 3: 게시판 기능 | ✅ | 공지사항, FAQ, 문의, 자료실, 자유게시판 |
| Phase 4-1: 데이터 표준 API | ✅ | 기본규격, 인증규격, 참여자별 처리절차 |
| Phase 4-1: 마이데이터 인증 API 규격 | ⬜ | **다음 작업** |

---

## 📐 코딩 컨벤션 (필수 준수)

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
| ❌ **No Factory Method** | `of()`, `from()` 정적 팩토리 메서드 사용 금지 |
| ❌ **No Setter (Entity/ResponseDto)** | Entity, ResponseDto에 Setter 금지 |
| ✅ **Setter (RequestDto)** | RequestDto에는 `@Setter` 필수 (폼 바인딩용) |
| ✅ **Use Mapper** | DTO ↔ Entity 변환은 별도 Mapper 클래스 사용 |
| ✅ **LAZY Loading** | `@ManyToOne`에 `fetch = FetchType.LAZY` 필수 |
| ✅ **N+1 방지** | JOIN FETCH 쿼리 사용 |

---

## 🔄 작업 방식

| 담당 | 작업 |
|------|------|
| **사용자** | 백엔드 Java 코드 직접 생성 |
| **Claude** | 프론트엔드 HTML 템플릿 생성, CSS 수정, 코드 제공 |

---

## 🎯 다음 작업: 마이데이터 인증 API 규격

### 핵심 구조

원본 사이트에서 확인한 결과, **마이데이터 인증 API 규격**은 API가이드의 하위가 아니라 **별도 섹션**입니다.

**원본 사이트 구조:**
```
API가이드 (GNB 메뉴)
├── API가이드 (데이터 표준 API)     ← 별도 사이드바 "API가이드"
│   ├── 데이터 표준 API 기본규격     ✅ 완료
│   ├── 데이터 표준 API 인증규격     ✅ 완료
│   └── 마이데이터 참여자별 API 처리 절차  ✅ 완료
│
├── 마이데이터 인증 API 규격         ← 별도 사이드바 "마이데이터 인증 API 규격"
│   ├── 개별인증 API                 ⬜ 다음 작업
│   └── 통합인증 API                 ⬜ 다음 작업
│
├── 마이데이터 지원 API 규격         ← 별도 사이드바 (스캔 필요)
└── 마이데이터 정보제공 API 규격     ← 별도 사이드바 (스캔 필요)
```

### CertApiController 템플릿 (사용자가 생성)

```java
package com.mydata.mydatatestbed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cert-api")
public class CertApiController {

    /**
     * /cert-api 접속 시 개별인증 API 페이지로 리다이렉트
     */
    @GetMapping
    public String redirectToIndividual() {
        return "redirect:/cert-api/individual";
    }

    /**
     * 개별인증 API
     */
    @GetMapping("/individual")
    public String individualApi(Model model) {
        model.addAttribute("sidebarMenus", getSidebarMenus());
        model.addAttribute("currentMenu", "/cert-api/individual");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("개별인증 API"));
        return "cert-api/individual-api";
    }

    /**
     * 통합인증 API
     */
    @GetMapping("/integrated")
    public String integratedApi(Model model) {
        model.addAttribute("sidebarMenus", getSidebarMenus());
        model.addAttribute("currentMenu", "/cert-api/integrated");
        model.addAttribute("breadcrumbItems", getBreadcrumbItems("통합인증 API"));
        return "cert-api/integrated-api";
    }

    // 마이데이터 인증 API 규격 사이드바 메뉴
    private List<Map<String, String>> getSidebarMenus() {
        return List.of(
                Map.of("name", "개별인증 API", "url", "/cert-api/individual"),
                Map.of("name", "통합인증 API", "url", "/cert-api/integrated")
        );
    }

    // 브레드크럼 생성
    private List<Map<String, String>> getBreadcrumbItems(String current) {
        return List.of(
                Map.of("name", "홈", "url", "/"),
                Map.of("name", "API가이드", "url", "/api-guide"),
                Map.of("name", "마이데이터 인증 API 규격", "url", "/cert-api"),
                Map.of("name", current, "url", "")
        );
    }
}
```

### ApiGuideController 사이드바 URL 수정 필요

기존 `getSidebarMenus()`에서 마이데이터 인증 API 규격 링크를 `/cert-api`로 변경:

```java
// 변경 전
Map.of("name", "마이데이터 인증 API 규격", "url", "/api-guide/auth-api"),
// 변경 후
Map.of("name", "마이데이터 인증 API 규격", "url", "/cert-api"),
```

마찬가지로 지원/정보제공도 추후 별도 컨트롤러 URL로 변경 예정:
```java
Map.of("name", "마이데이터 지원 API 규격", "url", "/support-api"),
Map.of("name", "마이데이터 정보제공 API 규격", "url", "/info-api")
```

### SecurityConfig 수정 필요

```java
// 추가 필요
.requestMatchers("/cert-api/**").permitAll()
```

### 스캔 완료된 데이터: 개별인증 API

원본 URL: `https://developers.mydatakorea.org/mdtb/apg/mac/bas/FSAG0201?id=7`

**페이지 구성** (Table of Contents):
- 개별인증-001: 인가코드 발급 요청 (v0, v2)
- 개별인증-002: 접근토큰 발급 요청 (v0, v2)
- 개별인증-003: 접근토큰 갱신 (v0, v2)
- 개별인증-004: 접근토큰 폐기 (v0, v2)

**각 API 스펙 구조** (반복 패턴):
```
API 제목 | 버전 | 날짜
─────────────────────────
기본 정보 테이블:
  API ID, HTTP Method, API 제공자, API 요청자, API명(URI), 설명, 기준시점, Content-Type

요청 메시지 명세 테이블:
  HTTP | 항목명 | 항목설명 | 필수 | 타입(길이) | 설명(비고)

응답 메시지 명세 테이블:
  HTTP | 항목명 | 항목설명 | 필수 | 타입(길이) | 설명(비고)
```

### 스캔 대기: 통합인증 API

원본 URL에서 사이드바 "통합인증 API" 클릭 시 이동하는 페이지를 스캔해야 함.
예상 구성: 통합인증-001, 002, 003

---

## 📂 Phase 4-1 완료된 파일 구조

```
src/main/java/com/mydata/mydatatestbed/controller/
└── ApiGuideController.java       # 데이터 표준 API 3개 페이지

src/main/resources/templates/api-guide/
├── basic-spec.html               # /api-guide/base     (기본규격)
├── auth-spec.html                # /api-guide/auth     (인증규격)
└── process-spec.html             # /api-guide/process  (참여자별 처리 절차)

src/main/resources/static/css/
└── api-guide.css                 # API 가이드 전용 CSS
```

### 다음 생성할 파일 (마이데이터 인증 API 규격)

```
src/main/java/com/mydata/mydatatestbed/controller/
└── CertApiController.java        # 사용자가 생성 (위 템플릿 참고)

src/main/resources/templates/cert-api/
├── individual-api.html           # /cert-api/individual (개별인증 API)
└── integrated-api.html           # /cert-api/integrated (통합인증 API)
```

---

## 🐛 해결된 트러블슈팅 요약 (Phase 4 관련)

| 문제 | 원인 | 해결 |
|------|------|------|
| `/api-guide` 접속 시 404 | URL을 `/api-guide/base`로 변경 후 기본 매핑 없음 | `redirectToBase()` 메서드 추가 |
| Thymeleaf 정적 클래스 접근 제한 | Thymeleaf 보안 정책으로 `T(...)` 문법 사용 불가 | 대체 템플릿 표현식 사용 |

---

## 💬 다음 세션 시작하기

1. **CertApiController.java** 생성 (위 템플릿 참고)
2. **ApiGuideController** 사이드바 URL 수정 (`/cert-api`)
3. **SecurityConfig**에 `/cert-api/**` permitAll 추가
4. 원본 사이트에서 **통합인증 API** 스캔
5. `individual-api.html`, `integrated-api.html` 생성

---

## 📚 관련 문서

| 문서 | 설명 |
|------|------|
| [README.md](README.md) | 프로젝트 소개, 빠른 시작 |
| [PROJECT_STATUS.md](PROJECT_STATUS.md) | 진행 상황, 파일 구조 |
| [API_SPEC.md](API_SPEC.md) | API 엔드포인트 상세 명세 |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | 트러블슈팅 가이드 |
