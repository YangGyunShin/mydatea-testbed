package com.mydata.mydatatestbed.mapper;

import com.mydata.mydatatestbed.dto.faq.FaqResponseDto;
import com.mydata.mydatatestbed.entity.Faq;
import org.springframework.stereotype.Component;

/**
 * FAQ 도메인 Mapper
 *
 * Entity ↔ DTO 변환 담당
 */
@Component
public class FaqMapper {

    /**
     * Faq Entity → 응답 DTO 변환
     *
     * @param faq Faq Entity
     * @return FaqResponseDto
     */
    public FaqResponseDto toResponseDto(Faq faq) {
        return FaqResponseDto.builder()
                .id(faq.getId())
                .category(faq.getCategory())
                .categoryDisplayName(faq.getCategory().getDisplayName())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .orderNum(faq.getOrderNum())
                .createdAt(faq.getCreatedAt())
                .build();
    }
}