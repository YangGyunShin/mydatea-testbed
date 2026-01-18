package com.mydata.mydatatestbed.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 비밀번호 일치 검증 로직
 *
 * ConstraintValidator<A, T> 인터페이스:
 * - A: 처리할 어노테이션 타입 (PasswordMatching)
 * - T: 검증 대상 타입 (Object - 어떤 클래스든 적용 가능하도록)
 *
 * 동작 흐름:
 * 1. @Valid가 붙은 객체 검증 시 Spring이 이 Validator 호출
 * 2. initialize()로 어노테이션 속성값 읽기
 * 3. isValid()로 실제 검증 수행
 * 4. false 반환 시 BindingResult에 에러 추가
 */
public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {

    private String passwordField;
    private String passwordConfirmField;

    /**
     * 어노테이션 속성값 초기화
     *
     * @param annotation 클래스에 붙은 @PasswordMatching 어노테이션
     */
    @Override
    public void initialize(PasswordMatching annotation) {
        this.passwordField = annotation.password();
        this.passwordConfirmField = annotation.passwordConfirm();
    }

    /**
     * 실제 검증 로직
     *
     * @param value 검증 대상 객체 (예: MemberSignupRequestDto 인스턴스)
     * @param context 검증 컨텍스트 (에러 메시지 커스터마이징 등에 사용)
     * @return true: 검증 통과, false: 검증 실패
     *
     * BeanWrapperImpl:
     * - Spring이 제공하는 리플렉션 유틸리티
     * - 필드명(String)으로 객체의 프로퍼티 값을 동적으로 가져올 수 있음
     * - 예: beanWrapper.getPropertyValue("password") → dto.getPassword() 호출
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // null 객체는 다른 어노테이션(@NotNull)이 처리하도록 통과시킴
        if (value == null) {
            return true;
        }

        // 리플렉션으로 필드값 가져오기
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object password = beanWrapper.getPropertyValue(passwordField);
        Object passwordConfirm = beanWrapper.getPropertyValue(passwordConfirmField);

        // 둘 다 null이면 일치 (둘 다 입력 안 함)
        if (password == null) {
            return passwordConfirm == null;
        }

        // 값 비교
        return password.equals(passwordConfirm);
    }
}