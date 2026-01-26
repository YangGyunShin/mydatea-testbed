package com.mydata.mydatatestbed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resources")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 파일 저장 경로 (서버 내부 경로)
     */
    @Column(nullable = false, length = 500)
    private String filePath;

    /**
     * 원본 파일명 (사용자가 업로드한 파일명)
     */
    @Column(nullable = false, length = 200)
    private String fileName;

    /**
     * 파일 크기 (bytes)
     */
    private Long fileSize;

    /**
     * 조회수
     */
    @Column(nullable = false)
    private int viewCount;

    /**
     * 다운로드 횟수
     */
    @Column(nullable = false)
    private int downloadCount;

    /**
     * 작성자 (관리자)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Builder
    private Resource(String title, String description, String filePath,
                     String fileName, Long fileSize, Member author) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.viewCount = 0;
        this.downloadCount = 0;
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
     * 다운로드 횟수 증가
     */
    public void incrementDownloadCount() {
        this.downloadCount++;
    }

    /**
     * 자료 정보 수정
     */
    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * 파일 정보 업데이트
     */
    public void updateFile(String filePath, String fileName, Long fileSize) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    /**
     * 파일 크기를 사람이 읽기 쉬운 형태로 변환
     */
    public String getFormattedFileSize() {
        if (fileSize == null || fileSize == 0) {
            return "0 B";
        }

        final String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double size = fileSize;

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.1f %s", size, units[unitIndex]);
    }
}