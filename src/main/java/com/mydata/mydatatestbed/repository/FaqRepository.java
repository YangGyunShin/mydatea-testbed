package com.mydata.mydatatestbed.repository;

import com.mydata.mydatatestbed.entity.Enum.FaqCategory;
import com.mydata.mydatatestbed.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * FAQ Repository
 *
 * FAQ 데이터 접근 계층
 */
public interface FaqRepository extends JpaRepository<Faq, Long> {

    /**
     * 활성화된 FAQ 전체 조회 (정렬순서 → 생성일 순)
     */
    @Query("SELECT f FROM Faq f WHERE f.active = true ORDER BY f.orderNum ASC, f.createdAt DESC")
    List<Faq> findAllActiveOrderByOrderNum();

    /**
     * 특정 카테고리의 활성화된 FAQ 조회
     */
    @Query("SELECT f FROM Faq f WHERE f.active = true AND f.category = :category ORDER BY f.orderNum ASC, f.createdAt DESC")
    List<Faq> findByCategoryAndActiveOrderByOrderNum(@Param("category") FaqCategory category);

    /**
     * 카테고리별 FAQ 개수 조회
     */
    long countByCategoryAndActiveTrue(FaqCategory category);
}