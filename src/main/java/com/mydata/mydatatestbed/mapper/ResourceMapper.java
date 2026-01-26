package com.mydata.mydatatestbed.mapper;

import com.mydata.mydatatestbed.dto.resource.ResourceListResponseDto;
import com.mydata.mydatatestbed.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceListResponseDto toListResponseDto(Resource resource) {
        return ResourceListResponseDto.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .fileName(resource.getFileName())
                .formattedFileSize(resource.getFormattedFileSize())
                .downloadCount(resource.getDownloadCount())
                .authorName(resource.getAuthor() != null ? resource.getAuthor().getName() : "관리자")
                .createdAt(resource.getCreatedAt())
                .build();
    }
}