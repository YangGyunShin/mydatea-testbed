package com.mydata.mydatatestbed.mapper;

import com.mydata.mydatatestbed.dto.notice.NoticeDetailResponseDto;
import com.mydata.mydatatestbed.dto.notice.NoticeListResponseDto;
import com.mydata.mydatatestbed.entity.Notice;
import org.springframework.stereotype.Component;

/**
 * Notice 도메인 Mapper
 *
 * Entity ↔ DTO 변환 담당
 */
@Component
public class NoticeMapper {

    /**
     * Notice Entity → 목록 응답 DTO 변환
     *
     * @param notice Notice Entity
     * @return 목록용 DTO (필요한 정보만 포함)
     */
    public NoticeListResponseDto toListResponseDto(Notice notice) {
        return NoticeListResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .pinned(notice.isPinned())
                .viewCount(notice.getViewCount())
                .hasAttachment(notice.hasAttachment())
                .authorName(notice.getAuthor() != null ? notice.getAuthor().getName() : null)
                .createdAt(notice.getCreatedAt())
                .build();
    }

    /**
     * Notice Entity → 상세 응답 DTO 변환
     *
     * @param notice Notice Entity
     * @return 상세용 DTO (전체 정보 포함)
     */
    public NoticeDetailResponseDto toDetailResponseDto(Notice notice) {
        return NoticeDetailResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .pinned(notice.isPinned())
                .viewCount(notice.getViewCount())
                .attachmentPath(notice.getAttachmentPath())
                .attachmentName(notice.getAttachmentName())
                .authorName(notice.getAuthor() != null ? notice.getAuthor().getName() : null)
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}