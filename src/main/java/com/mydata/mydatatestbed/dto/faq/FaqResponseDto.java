package com.mydata.mydatatestbed.dto.faq;

import com.mydata.mydatatestbed.entity.Enum.FaqCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * FAQ 응답 DTO
 *
 * FAQ 목록 및 상세 조회 시 사용
 */
@Getter
@Builder
public class FaqResponseDto {

    private Long id;
    private FaqCategory category;
    private String categoryDisplayName;  // 카테고리 한글명
    private String question;
    private String answer;
    private int orderNum;
    private LocalDateTime createdAt;
}