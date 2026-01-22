package com.mydata.mydatatestbed.service.impl;

import com.mydata.mydatatestbed.dto.notice.NoticeDetailResponseDto;
import com.mydata.mydatatestbed.dto.notice.NoticeListResponseDto;
import com.mydata.mydatatestbed.entity.Notice;
import com.mydata.mydatatestbed.mapper.NoticeMapper;
import com.mydata.mydatatestbed.repository.NoticeRepository;
import com.mydata.mydatatestbed.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Notice Service 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    @Override
    public Page<NoticeListResponseDto> getNoticeList(Pageable pageable) {
        return noticeRepository.findAllOrderByPinnedAndCreatedAt(pageable)
                .map(noticeMapper::toListResponseDto);
    }

    @Override
    public Page<NoticeListResponseDto> searchNotices(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return getNoticeList(pageable);
        }
        return noticeRepository.findByKeyword(keyword.trim(), pageable)
                .map(noticeMapper::toListResponseDto);
    }

    @Override
    public List<NoticeListResponseDto> getRecentNotices(int count) {
        return noticeRepository.findTopN(PageRequest.of(0, count))
                .stream()
                .map(noticeMapper::toListResponseDto)
                .toList();
    }

    @Override
    public NoticeDetailResponseDto getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));

        return noticeMapper.toDetailResponseDto(notice);
    }

    @Override
    @Transactional
    public NoticeDetailResponseDto getNoticeDetailWithViewCount(Long id) {
        Notice notice = noticeRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + id));

        // 조회수 증가 (Dirty Checking으로 자동 UPDATE)
        // incrementViewCount() 호출만으로 UPDATE 쿼리 자동 실행
        notice.incrementViewCount();

        return noticeMapper.toDetailResponseDto(notice);
    }
}