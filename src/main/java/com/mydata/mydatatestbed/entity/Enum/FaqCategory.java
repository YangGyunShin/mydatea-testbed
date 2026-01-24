package com.mydata.mydatatestbed.entity.Enum;

/**
 * FAQ 카테고리 열거형
 *
 * FAQ 질문을 주제별로 분류하기 위한 카테고리
 * 각 카테고리는 표시용 한글명(displayName)을 가짐
 */
public enum FaqCategory {

    GENERAL("일반"),
    SIGNUP("회원가입"),
    API("API"),
    TEST("테스트"),
    CONFORMANCE("적합성심사");

    /**
     * 화면에 표시할 카테고리명
     */
    private final String displayName;

    FaqCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}