package com.mydata.mydatatestbed.dto.notice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공지사항 상세 응답 DTO
 * 상세 페이지에서 필요한 전체 정보 포함
 */
@Getter
@Builder
public class NoticeDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private boolean pinned;
    private int viewCount;
    private String attachmentPath;
    private String attachmentName;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}