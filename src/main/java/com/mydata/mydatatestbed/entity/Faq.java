package com.mydata.mydatatestbed.entity;

import com.mydata.mydatatestbed.entity.Enum.FaqCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * FAQ(자주 묻는 질문) 엔티티
 *
 * 사용자들이 자주 묻는 질문과 답변을 관리
 * 카테고리별로 분류되며, 정렬 순서(orderNum)로 표시 순서 제어
 */
@Entity
@Table(name = "faqs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * FAQ 카테고리 (일반, 회원가입, API, 테스트, 적합성심사)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FaqCategory category;

    /**
     * 질문 내용
     */
    @Column(nullable = false, length = 300)
    private String question;

    /**
     * 답변 내용
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    /**
     * 정렬 순서 (낮을수록 먼저 표시)
     */
    @Column(nullable = false)
    private int orderNum;

    /**
     * 활성화 여부 (false면 화면에 표시 안 함)
     */
    @Column(nullable = false)
    private boolean active;

    @Builder
    private Faq(FaqCategory category, String question, String answer, int orderNum, boolean active) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.orderNum = orderNum;
        this.active = active;
    }

    // === 비즈니스 메서드 === //

    /**
     * FAQ 내용 수정
     */
    public void update(FaqCategory category, String question, String answer, int orderNum) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.orderNum = orderNum;
    }

    /**
     * 활성화/비활성화 토글
     */
    public void toggleActive() {
        this.active = !this.active;
    }

    /**
     * 비활성화
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * 활성화
     */
    public void activate() {
        this.active = true;
    }
}