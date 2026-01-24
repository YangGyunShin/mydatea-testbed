package com.mydata.mydatatestbed.service.impl;

import com.mydata.mydatatestbed.dto.faq.FaqResponseDto;
import com.mydata.mydatatestbed.entity.Enum.FaqCategory;
import com.mydata.mydatatestbed.mapper.FaqMapper;
import com.mydata.mydatatestbed.repository.FaqRepository;
import com.mydata.mydatatestbed.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FAQ Service 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;

    @Override
    public List<FaqResponseDto> getAllFaqs() {
        return faqRepository.findAllActiveOrderByOrderNum()
                .stream()
                .map(faqMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<FaqResponseDto> getFaqsByCategory(FaqCategory category) {
        return faqRepository.findByCategoryAndActiveOrderByOrderNum(category)
                .stream()
                .map(faqMapper::toResponseDto)
                .toList();
    }
}