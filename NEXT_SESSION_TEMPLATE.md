# 📋 다음 세션 작업 가이드

> **마지막 업데이트**: 2025-01-25  
> **다음 작업**: Phase 3-4 자료실 (Resource) 또는 Phase 3-5 자유게시판 (Board)

---

## 📁 프로젝트 정보

| 항목 | 내용 |
|------|------|
| **프로젝트명** | 금융분야 마이데이터 테스트베드 클론 |
| **프로젝트 경로** | `~/Library/Mobile Documents/com~apple~CloudDocs/Spring/study/mydata-testbed` |
| **기술 스택** | Spring Boot 3.4.1, Java 21, Thymeleaf, Spring Security 6.x, JPA, H2 |

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
| ❌ **No Setter** | Entity, ResponseDto에 Setter 금지 (RequestDto만 예외) |
| ✅ **Use Mapper** | DTO ↔ Entity 변환은 별도 Mapper 클래스 사용 |
| ✅ **LAZY Loading** | `@ManyToOne`에 `fetch = FetchType.LAZY` 필수 |
| ✅ **N+1 방지** | JOIN FETCH 쿼리 사용 |

---

## 🔗 남은 URL 매핑

### 자료실 (Resource)

| URL | Method | 인증 | 설명 |
|-----|--------|------|------|
| `/support/resource` | GET | ❌ | 자료 목록 |
| `/support/resource/{id}/download` | GET | ❌ | 자료 다운로드 |

### 자유게시판 (Board)

| URL | Method | 인증 | 설명 |
|-----|--------|------|------|
| `/support/board` | GET | ❌ | 게시글 목록 |
| `/support/board/{id}` | GET | ❌ | 게시글 상세 |
| `/support/board/write` | GET | ✅ | 글쓰기 폼 |
| `/support/board/write` | POST | ✅ | 글 등록 |

---

## 📂 생성할 파일 경로

### 자료실 (Resource)

```
src/main/java/com/mydata/mydatatestbed/
├── entity/
│   └── Resource.java
├── repository/
│   └── ResourceRepository.java
├── dto/resource/
│   └── ResourceListResponseDto.java
├── mapper/
│   └── ResourceMapper.java
└── service/
    ├── ResourceService.java
    └── impl/ResourceServiceImpl.java

src/main/resources/templates/support/
└── resource-list.html
```

### 자유게시판 (Board)

```
src/main/java/com/mydata/mydatatestbed/
├── entity/
│   └── Board.java
├── repository/
│   └── BoardRepository.java
├── dto/board/
│   ├── BoardRequestDto.java
│   ├── BoardListResponseDto.java
│   └── BoardDetailResponseDto.java
├── mapper/
│   └── BoardMapper.java
└── service/
    ├── BoardService.java
    └── impl/BoardServiceImpl.java

src/main/resources/templates/support/
├── board-list.html
├── board-detail.html
└── board-write.html
```

---

## 🔄 작업 방식

| 담당 | 작업 |
|------|------|
| **사용자** | 백엔드 Java 코드 직접 생성 |
| **Claude** | 프론트엔드 HTML 템플릿 생성, CSS 수정, 코드 제공 |

### 작업 순서

1. Claude가 백엔드 코드 (Entity, Repository, DTO, Mapper, Service, Controller) 제공
2. 사용자가 해당 Java 파일들을 수동으로 생성
3. Claude가 프론트엔드 템플릿 직접 생성
4. 테스트 및 디버깅

---

## 📚 코드 예시 (참고용)

### Entity 예시 (Inquiry.java)

```java
@Entity
@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status = InquiryStatus.WAITING;

    @Builder
    public Inquiry(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
```

### Repository 예시 (InquiryRepository.java)

```java
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i WHERE i.member = :member ORDER BY i.createdAt DESC")
    Page<Inquiry> findByMemberOrderByCreatedAtDesc(@Param("member") Member member, Pageable pageable);

    @Query("SELECT i FROM Inquiry i " +
           "LEFT JOIN FETCH i.member " +
           "LEFT JOIN FETCH i.answerer " +
           "WHERE i.id = :id")
    Optional<Inquiry> findByIdWithMemberAndAnswerer(@Param("id") Long id);

    long countByMember(Member member);
}
```

### Mapper 예시 (InquiryMapper.java)

```java
@Component
public class InquiryMapper {

    public Inquiry toEntity(InquiryRequestDto dto, Member member) {
        return Inquiry.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    public InquiryListResponseDto toListResponseDto(Inquiry inquiry) {
        return InquiryListResponseDto.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .status(inquiry.getStatus())
                .statusDisplayName(inquiry.getStatus().getDisplayName())
                .createdAt(inquiry.getCreatedAt())
                .answeredAt(inquiry.getAnsweredAt())
                .build();
    }

    public InquiryResponseDto toResponseDto(Inquiry inquiry) {
        return InquiryResponseDto.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .memberName(inquiry.getMember().getName())
                .memberEmail(inquiry.getMember().getEmail().getValue())
                .status(inquiry.getStatus())
                .statusDisplayName(inquiry.getStatus().getDisplayName())
                .answer(inquiry.getAnswer())
                .answererName(inquiry.getAnswerer() != null ? inquiry.getAnswerer().getName() : null)
                .answeredAt(inquiry.getAnsweredAt())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .build();
    }
}
```

### Service 예시 (InquiryServiceImpl.java)

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryMapper inquiryMapper;

    @Override
    @Transactional
    public Long createInquiry(Member member, InquiryRequestDto requestDto) {
        Inquiry inquiry = inquiryMapper.toEntity(requestDto, member);
        return inquiryRepository.save(inquiry).getId();
    }

    @Override
    public Page<InquiryListResponseDto> getMyInquiries(Member member, Pageable pageable) {
        return inquiryRepository.findByMemberOrderByCreatedAtDesc(member, pageable)
                .map(inquiryMapper::toListResponseDto);
    }

    @Override
    public InquiryResponseDto getInquiryDetail(Long id, Member member) {
        Inquiry inquiry = inquiryRepository.findByIdWithMemberAndAnswerer(id)
                .orElseThrow(() -> new IllegalArgumentException("문의를 찾을 수 없습니다: " + id));
        
        if (!inquiry.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        
        return inquiryMapper.toResponseDto(inquiry);
    }

    @Override
    public long countMyInquiries(Member member) {
        return inquiryRepository.countByMember(member);
    }
}
```

### Controller 패턴 (SupportController - Sidebar)

```java
// 사이드바 메뉴 생성 (공통 유틸)
private List<Map<String, String>> createSupportSidebarMenus() {
    return List.of(
        Map.of("name", "공지사항", "url", "/support/notice"),
        Map.of("name", "FAQ", "url", "/support/faq"),
        Map.of("name", "문의하기", "url", "/support/inquiry"),
        Map.of("name", "자료실", "url", "/support/resource"),
        Map.of("name", "자유게시판", "url", "/support/board")
    );
}

// 브레드크럼 생성 (공통 유틸)
private List<Map<String, String>> createInquiryBreadcrumb(String pageName, String pageUrl) {
    return List.of(
        Map.of("name", "고객지원", "url", "/support/notice"),
        Map.of("name", pageName, "url", pageUrl)
    );
}
```

---

## 📚 관련 문서

| 문서 | 설명 |
|------|------|
| [README.md](README.md) | 프로젝트 소개, 빠른 시작 |
| [PROJECT_STATUS.md](PROJECT_STATUS.md) | 진행 상황, 파일 구조 |
| [API_SPEC.md](API_SPEC.md) | API 엔드포인트 상세 명세 |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | 트러블슈팅 가이드 |

---

## 💬 시작하기

위 내용을 확인하시고, **자료실(Resource) 또는 자유게시판(Board)** 중 원하시는 기능부터 구현을 시작해주세요.

기존 Inquiry 패턴을 참고하여:
1. 먼저 백엔드 코드 (Entity, Repository, DTO, Mapper, Service)를 제공해드립니다
2. 그 다음 SupportController에 추가할 엔드포인트 코드를 제공해드립니다
3. 마지막으로 HTML 템플릿을 생성합니다

감사합니다! 🙏
