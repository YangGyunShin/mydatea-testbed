package com.mydata.mydatatestbed.service;

import com.mydata.mydatatestbed.dto.faq.FaqResponseDto;
import com.mydata.mydatatestbed.entity.Enum.FaqCategory;

import java.util.List;

/**
 * FAQ Service 인터페이스
 *
 * FAQ 관련 유스케이스 정의
 */
public interface FaqService {

    /**
     * 전체 FAQ 목록 조회 (활성화된 것만)
     *
     * @return FAQ 목록
     */
    List<FaqResponseDto> getAllFaqs();

    /**
     * 카테고리별 FAQ 목록 조회
     *
     * @param category FAQ 카테고리
     * @return 해당 카테고리의 FAQ 목록
     */
    List<FaqResponseDto> getFaqsByCategory(FaqCategory category);
}