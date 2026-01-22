package com.mydata.mydatatestbed.service;

import com.mydata.mydatatestbed.dto.notice.NoticeDetailResponseDto;
import com.mydata.mydatatestbed.dto.notice.NoticeListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Notice Service 인터페이스
 *
 * 공지사항 관련 유스케이스 정의
 */
public interface NoticeService {

    /**
     * 공지사항 목록 조회 (페이징)
     *
     * @param pageable 페이징 정보
     * @return 공지사항 목록 (Page)
     */
    Page<NoticeListResponseDto> getNoticeList(Pageable pageable);

    /**
     * 공지사항 검색 (페이징)
     *
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 공지사항 목록 (Page)
     */
    Page<NoticeListResponseDto> searchNotices(String keyword, Pageable pageable);

    /**
     * 최신 공지사항 조회 (메인 페이지용)
     *
     * @param count 조회할 개수
     * @return 최신 공지사항 목록
     */
    List<NoticeListResponseDto> getRecentNotices(int count);

    /**
     * 공지사항 상세 조회
     *
     * @param id 공지사항 ID
     * @return 공지사항 상세 정보
     */
    NoticeDetailResponseDto getNoticeDetail(Long id);

    /**
     * 공지사항 상세 조회 + 조회수 증가
     *
     * @param id 공지사항 ID
     * @return 공지사항 상세 정보
     */
    NoticeDetailResponseDto getNoticeDetailWithViewCount(Long id);
}