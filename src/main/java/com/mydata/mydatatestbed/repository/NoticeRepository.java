package com.mydata.mydatatestbed.repository;

import com.mydata.mydatatestbed.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    /**
     * 공지사항 목록 조회 (중요 공지 먼저, 그 다음 최신순)
     */
    @Query("SELECT n FROM Notice n ORDER BY n.pinned DESC, n.createdAt DESC")
    Page<Notice> findAllOrderByPinnedAndCreatedAt(Pageable pageable);

    /**
     * 키워드로 검색 (제목 또는 내용에 포함)
     */
    @Query("SELECT n FROM Notice n " +
            "WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% " +
            "ORDER BY n.pinned DESC, n.createdAt DESC")
    Page<Notice> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 최신 공지사항 N개 조회 (메인 페이지용)
     */
    @Query("SELECT n FROM Notice n ORDER BY n.pinned DESC, n.createdAt DESC")
    List<Notice> findTopN(Pageable pageable);

    /**
     * 중요 공지사항만 조회
     */
    List<Notice> findByPinnedTrueOrderByCreatedAtDesc();

    /**
     * 공지사항 상세 조회 (작성자 함께 조회 - N+1 방지)
     */
    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.id = :id")
    Optional<Notice> findByIdWithAuthor(@Param("id") Long id);
}