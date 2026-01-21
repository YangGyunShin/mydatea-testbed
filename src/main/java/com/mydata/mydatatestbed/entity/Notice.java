package com.mydata.mydatatestbed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 중요 공지 여부 (상단 고정)
     */
    @Column(nullable = false)
    private boolean pinned;

    @Column(nullable = false)
    private int viewCount;

    /**
     * 첨부파일 저장 경로 (서버 내부 경로)
     */
    @Column(length = 500)
    private String attachmentPath;

    /**
     * 첨부파일 원본 파일명 (사용자가 업로드한 파일명)
     */
    @Column(length = 200)
    private String attachmentName;

    /**
     * 작성자 (Member와 다대일 관계)
     *
     * @ManyToOne: 여러 Notice가 한 Member에 속함
     * @JoinColumn: FK 컬럼 설정
     *   - name = "author_id" → notices 테이블에 author_id 컬럼 생성
     *   - 이 컬럼이 members 테이블의 PK(id)를 참조하는 외래키가 됨
     * @FetchType.LAZY: 지연 로딩 (author 사용 시점에 쿼리 실행)
     *   - @ManyToOne 기본값은 EAGER이므로 명시적으로 LAZY 지정 권장
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Builder
    private Notice(String title, String content, boolean pinned,
                   String attachmentPath, String attachmentName, Member author) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.viewCount = 0;
        this.attachmentPath = attachmentPath;
        this.attachmentName = attachmentName;
        this.author = author;
    }

    // === 비즈니스 메서드 === //

    /**
     * 조회수 증가
     */
    public void incrementViewCount() {
        this.viewCount++;
    }

    /**
     * 공지사항 내용 수정
     */
    public void update(String title, String content, boolean pinned) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
    }

    /**
     * 첨부파일 업데이트
     */
    public void updateAttachment(String attachmentPath, String attachmentName) {
        this.attachmentPath = attachmentPath;
        this.attachmentName = attachmentName;
    }

    /**
     * 첨부파일 삭제
     */
    public void removeAttachment() {
        this.attachmentPath = null;
        this.attachmentName = null;
    }

    /**
     * 첨부파일 존재 여부 확인
     */
    public boolean hasAttachment() {
        return this.attachmentPath != null && !this.attachmentPath.isBlank();
    }
}