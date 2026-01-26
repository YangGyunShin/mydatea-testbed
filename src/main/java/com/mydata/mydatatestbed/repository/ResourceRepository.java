package com.mydata.mydatatestbed.repository;

import com.mydata.mydatatestbed.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * 전체 자료 목록 조회 (최신순)
     */
    @Query("SELECT r FROM Resource r " +
            "LEFT JOIN FETCH r.author " +
            "ORDER BY r.createdAt DESC")
    Page<Resource> findAllWithAuthor(Pageable pageable);

    /**
     * 제목 또는 설명으로 검색
     */
    @Query("SELECT r FROM Resource r " +
            "LEFT JOIN FETCH r.author " +
            "WHERE r.title LIKE %:keyword% OR r.description LIKE %:keyword% " +
            "ORDER BY r.createdAt DESC")
    Page<Resource> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * ID로 자료 조회 (author 함께 로드)
     */
    @Query("SELECT r FROM Resource r " +
            "LEFT JOIN FETCH r.author " +
            "WHERE r.id = :id")
    Optional<Resource> findByIdWithAuthor(@Param("id") Long id);

    /**
     * 다음 글 조회 (현재 글보다 최신인 글 중 가장 오래된 것)
     */
    @Query("SELECT r FROM Resource r WHERE r.createdAt > " +
            "(SELECT r2.createdAt FROM Resource r2 WHERE r2.id = :currentId) " +
            "ORDER BY r.createdAt ASC LIMIT 1")
    Optional<Resource> findNextResource(@Param("currentId") Long currentId);

    /**
     * 이전 글 조회 (현재 글보다 오래된 글 중 가장 최신인 것)
     */
    @Query("SELECT r FROM Resource r WHERE r.createdAt < " +
            "(SELECT r2.createdAt FROM Resource r2 WHERE r2.id = :currentId) " +
            "ORDER BY r.createdAt DESC LIMIT 1")
    Optional<Resource> findPrevResource(@Param("currentId") Long currentId);
}