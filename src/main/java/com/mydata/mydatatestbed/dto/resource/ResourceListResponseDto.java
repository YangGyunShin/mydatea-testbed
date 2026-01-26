package com.mydata.mydatatestbed.dto.resource;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResourceListResponseDto {

    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String formattedFileSize;
    private int downloadCount;
    private String authorName;
    private LocalDateTime createdAt;
}