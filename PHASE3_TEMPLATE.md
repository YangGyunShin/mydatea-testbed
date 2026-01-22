# 📋 Phase 3 작업 요청서 - 게시판 기능 (남은 작업)

## 프로젝트 정보

- **프로젝트명**: 금융분야 마이데이터 테스트베드 클론
- **프로젝트 경로**: `~/Library/Mobile Documents/com~apple~CloudDocs/Spring/study/mydata-testbed`
- **기술 스택**: Spring Boot 3.4.1, Java 21, Thymeleaf, Spring Security 6.x, JPA, H2 Database

---

## 완료된 작업

### Phase 1: 기본 구조 ✅
- 레이아웃 (Header, Footer, Sidebar)
- CSS/JS 파일
- MainController, SecurityConfig, AuditConfig

### Phase 2: 회원 기능 ✅
- Member Entity, VO (Email, Password, Phone)
- 회원가입 (4단계), 로그인/로그아웃
- 이메일 인증 기능

### Phase 3 (완료): 공지사항 ✅
- Notice Entity, Repository, DTO, Mapper, Service
- SupportController (공지사항 부분)
- notice-list.html, notice-detail.html
- 목록/상세/검색/페이징 기능

---

## 남은 Phase 3 작업

아래 기능들을 순서대로 구현해주세요.

### 1. FAQ 기능
- [ ] Faq Entity
- [ ] FaqCategory Enum (GENERAL, SIGNUP, API, TEST, CONFORMANCE)
- [ ] FaqRepository
- [ ] FaqResponseDto
- [ ] FaqMapper
- [ ] FaqService / FaqServiceImpl
- [ ] SupportController에 FAQ 엔드포인트 추가
- [ ] faq.html 템플릿 (아코디언 형태)

### 2. 문의하기 (Inquiry) 기능
- [ ] Inquiry Entity
- [ ] InquiryStatus Enum (WAITING, COMPLETED)
- [ ] InquiryRepository
- [ ] InquiryRequestDto, InquiryResponseDto
- [ ] InquiryMapper
- [ ] InquiryService / InquiryServiceImpl
- [ ] SupportController에 문의하기 엔드포인트 추가
- [ ] inquiry-form.html, inquiry-list.html 템플릿

### 3. 자료실 (Resource) 기능
- [ ] Resource Entity
- [ ] ResourceRepository
- [ ] ResourceListResponseDto, ResourceDetailResponseDto
- [ ] ResourceMapper
- [ ] ResourceService / ResourceServiceImpl
- [ ] SupportController에 자료실 엔드포인트 추가
- [ ] resource-list.html 템플릿
- [ ] 파일 다운로드 기능

### 4. 자유게시판 (Board) 기능
- [ ] Board Entity
- [ ] BoardRepository
- [ ] BoardListResponseDto, BoardDetailResponseDto, BoardRequestDto
- [ ] BoardMapper
- [ ] BoardService / BoardServiceImpl
- [ ] SupportController에 자유게시판 엔드포인트 추가
- [ ] board-list.html, board-detail.html, board-write.html 템플릿

---

## 코딩 컨벤션 (필수 준수)

### 클래스별 어노테이션

| 클래스 | 어노테이션 |
|--------|-----------|
| **Entity** | `@Getter @NoArgsConstructor(access = PROTECTED)` + 생성자에 `@Builder` |
| **ResponseDto** | `@Getter @Builder` |
| **RequestDto** | `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor` |

### 규칙
- **No Factory Method**: `of()`, `from()` 등 정적 팩토리 메서드 사용 금지
- **No Setter**: Entity, ResponseDto에는 Setter 금지 (RequestDto만 예외)
- **Use Mapper**: DTO ↔ Entity 변환은 별도 Mapper 클래스 사용
- **@ManyToOne**: 반드시 `fetch = FetchType.LAZY` 지정
- **N+1 방지**: JOIN FETCH 쿼리 사용

---

## 참고할 기존 코드 (Notice 패턴 따르기)

### Entity 예시 (Notice.java)
```java
@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Builder
    public Notice(String title, String content, boolean pinned, Member author) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.viewCount = 0;
        this.author = author;
    }
}
```

### Repository 예시 (NoticeRepository.java)
```java
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.id = :id")
    Optional<Notice> findByIdWithAuthor(@Param("id") Long id);
}
```

### Mapper 예시 (NoticeMapper.java)
```java
@Component
public class NoticeMapper {
    
    public NoticeListResponseDto toListDto(Notice notice) {
        return NoticeListResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .authorName(notice.getAuthor() != null ? notice.getAuthor().getName() : "관리자")
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
```

---

## 파일 경로 참고

```
src/main/java/com/mydata/mydatatestbed/
├── entity/
│   └── Faq.java, Inquiry.java, Resource.java, Board.java
├── entity/Enum/
│   └── FaqCategory.java, InquiryStatus.java
├── repository/
│   └── FaqRepository.java, InquiryRepository.java, ...
├── dto/
│   ├── faq/
│   ├── inquiry/
│   ├── resource/
│   └── board/
├── mapper/
│   └── FaqMapper.java, InquiryMapper.java, ...
├── service/
│   └── FaqService.java, InquiryService.java, ...
└── service/impl/
    └── FaqServiceImpl.java, InquiryServiceImpl.java, ...

src/main/resources/templates/support/
├── faq.html
├── inquiry-form.html
├── inquiry-list.html
├── resource-list.html
├── board-list.html
├── board-detail.html
└── board-write.html
```

---

## 요청사항

1. **FAQ부터 시작**해주세요.
2. 각 기능 구현 후 **README.md 개발 로드맵 업데이트** 부탁드립니다.
3. 새로운 트러블슈팅이 발생하면 **TROUBLESHOOTING.md에 추가**해주세요.
4. **기존 Notice 코드 패턴을 참고**하여 일관성 있게 구현해주세요.

---

## 참고 문서

| 문서 | 경로 |
|------|------|
| README | `README.md` |
| API 명세 | `API_SPEC.md` |
| 트러블슈팅 | `TROUBLESHOOTING.md` |
| 시스템 구조 | `마이데이터_테스트베드_시스템_구조` (프로젝트 파일) |
