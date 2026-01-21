package com.mydata.mydatatestbed.dto.notice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공지사항 목록 응답 DTO
 * 목록 페이지에서 필요한 정보만 포함
 */
@Getter
@Builder
public class NoticeListResponseDto {

    private Long id;
    private String title;
    private boolean pinned;
    private int viewCount;
    private boolean hasAttachment;
    private String authorName;
    private LocalDateTime createdAt;
}