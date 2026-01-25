# 📋 다음 세션 작업 요청서

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

## ✅ 완료된 작업

| Phase | 기능 | 상태 |
|-------|------|------|
| Phase 1 | 기본 구조 (레이아웃, CSS/JS, Config) | ✅ 완료 |
| Phase 2 | 회원 기능 (회원가입 4단계, 로그인, 이메일 인증) | ✅ 완료 |
| Phase 3-1 | 공지사항 (Notice) | ✅ 완료 |
| Phase 3-2 | FAQ | ✅ 완료 |
| Phase 3-3 | 문의하기 (Inquiry) | ✅ 완료 |

---

## 📝 남은 작업

### 1. 자료실 (Resource) - 다음 구현

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

### 2. 자유게시판 (Board)

| 항목 | 상태 |
|------|------|
| Board Entity | ⬜ |
| BoardRepository | ⬜ |
| BoardRequestDto, BoardListResponseDto, BoardDetailResponseDto | ⬜ |
| BoardMapper | ⬜ |
| BoardService / BoardServiceImpl | ⬜ |
| SupportController (Board 부분) | ⬜ |
| board-list.html, board-detail.html, board-write.html | ⬜ |

---

## 📐 코딩 컨벤션 (필수 준수)

### 어노테이션 패턴

| 클래스 유형 | 어노테이션 |
|------------|-----------|
| **Entity** | `@Getter @NoArgsConstructor(access = PROTECTED)` + 생성자에 `@Builder` |
| **ResponseDto** | `@Getter @Builder` |
| **RequestDto** | `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor` |

### 필수 규칙

- ❌ **No Factory Method**: `of()`, `from()` 정적 팩토리 메서드 사용 금지
- ❌ **No Setter**: Entity, ResponseDto에 Setter 금지 (RequestDto만 예외)
- ✅ **Use Mapper**: DTO ↔ Entity 변환은 별도 Mapper 클래스 사용
- ✅ **@ManyToOne**: 반드시 `fetch = FetchType.LAZY` 지정
- ✅ **N+1 방지**: JOIN FETCH 쿼리 사용

---

## 🔗 URL 매핑 (예정)

### 자료실

| URL | Method | 설명 |
|-----|--------|------|
| `/support/resource` | GET | 자료 목록 |
| `/support/resource/{id}/download` | GET | 자료 다운로드 |

### 자유게시판

| URL | Method | 설명 |
|-----|--------|------|
| `/support/board` | GET | 게시글 목록 |
| `/support/board/{id}` | GET | 게시글 상세 |
| `/support/board/write` | GET | 글쓰기 폼 (로그인 필요) |
| `/support/board/write` | POST | 글 등록 |

---

## 📂 파일 경로

```
src/main/java/com/mydata/mydatatestbed/
├── entity/
│   └── Resource.java, Board.java
├── repository/
│   └── ResourceRepository.java, BoardRepository.java
├── dto/
│   ├── resource/
│   │   └── ResourceListResponseDto.java
│   └── board/
│       ├── BoardRequestDto.java
│       ├── BoardListResponseDto.java
│       └── BoardDetailResponseDto.java
├── mapper/
│   └── ResourceMapper.java, BoardMapper.java
├── service/
│   └── ResourceService.java, BoardService.java
└── service/impl/
    └── ResourceServiceImpl.java, BoardServiceImpl.java

src/main/resources/templates/support/
├── resource-list.html
├── board-list.html
├── board-detail.html
└── board-write.html
```

---

## 🔄 작업 방식

### 역할 분담

| 담당 | 작업 |
|------|------|
| **사용자** | 백엔드 Java 코드 직접 생성 |
| **Claude** | 프론트엔드 HTML 템플릿 생성, 필요 시 CSS 수정, 코드 제공 |

### 작업 순서

1. Claude가 백엔드 코드(Entity, Repository, DTO, Mapper, Service, Controller 수정분) 제공
2. 사용자가 해당 Java 파일들을 수동으로 생성
3. Claude가 프론트엔드 템플릿 직접 생성
4. 테스트 및 디버깅

---

## 📚 참고할 기존 코드 패턴

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

    /**
     * 문의 작성자 (회원)
     * 관계: Inquiry(N) : Member(1) - 다대일 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

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
           "WHERE i.id = :id")
    Optional<Inquiry> findByIdWithMember(@Param("id") Long id);
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
                .createdAt(inquiry.getCreatedAt())
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
}
```

---

## 📚 참고 문서

| 문서 | 설명 |
|------|------|
| `README.md` | 프로젝트 소개, 실행 방법 |
| `PROJECT_STATUS.md` | 전체 진행 상황, 파일 구조 |
| `API_SPEC.md` | API 엔드포인트 상세 명세 |
| `TROUBLESHOOTING.md` | 트러블슈팅 가이드 |

---

## 💬 시작하기

위 내용을 확인하시고, **자료실(Resource) 또는 자유게시판(Board)** 중 원하시는 기능부터 구현을 시작해주세요.

기존 Inquiry 패턴을 참고하여:
1. 먼저 백엔드 코드(Entity, Repository, DTO, Mapper, Service)를 제공해주세요
2. 그 다음 SupportController에 추가할 엔드포인트 코드를 제공해주세요
3. 마지막으로 HTML 템플릿을 생성해주세요

감사합니다! 🙏
