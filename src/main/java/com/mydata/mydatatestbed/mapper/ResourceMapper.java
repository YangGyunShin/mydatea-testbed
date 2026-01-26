package com.mydata.mydatatestbed.mapper;

import com.mydata.mydatatestbed.dto.resource.ResourceDetailResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceListResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceNavDto;
import com.mydata.mydatatestbed.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceListResponseDto toListResponseDto(Resource resource) {
        return ResourceListResponseDto.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .createdAt(resource.getCreatedAt())
                .build();
    }

    public ResourceDetailResponseDto toDetailResponseDto(Resource resource) {
        return ResourceDetailResponseDto.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .content(resource.getDescription())
                .fileName(resource.getFileName())
                .formattedFileSize(resource.getFormattedFileSize())
                .viewCount(resource.getViewCount())
                .downloadCount(resource.getDownloadCount())
                .authorName(resource.getAuthor() != null ? resource.getAuthor().getName() : "관리자")
                .createdAt(resource.getCreatedAt())
                .build();
    }

    public ResourceNavDto toNavDto(Resource resource) {
        return ResourceNavDto.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .build();
    }
}