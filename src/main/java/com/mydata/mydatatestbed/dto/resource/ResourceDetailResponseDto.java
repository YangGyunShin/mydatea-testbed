package com.mydata.mydatatestbed.dto.resource;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResourceDetailResponseDto {

    private Long id;
    private String title;
    private String content;       // description을 content로 표시
    private String fileName;
    private String formattedFileSize;
    private int viewCount;
    private int downloadCount;
    private String authorName;
    private LocalDateTime createdAt;
}