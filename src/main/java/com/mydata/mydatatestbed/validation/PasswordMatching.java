package com.mydata.mydatatestbed.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 비밀번호 일치 여부 검증 커스텀 어노테이션
 *
 * @Target(ElementType.TYPE): 클래스 레벨에 붙이는 어노테이션
 * - 두 필드를 비교해야 하므로 필드 레벨이 아닌 클래스 레벨에 적용
 *
 * @Retention(RetentionPolicy.RUNTIME): 런타임에 어노테이션 정보 유지
 * - Spring Validation이 런타임에 어노테이션을 읽어야 하므로 필수
 *
 * @Constraint(validatedBy = ...): 실제 검증 로직을 담당하는 Validator 클래스 지정
 *
 * 사용 예시:
 * - 기본: @PasswordMatching
 * - 필드명 지정: @PasswordMatching(password = "newPwd", passwordConfirm = "newPwdConfirm")
 * - 메시지 변경: @PasswordMatching(message = "새 비밀번호가 일치하지 않습니다.")
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchingValidator.class)
public @interface PasswordMatching {

    String message() default "비밀번호가 일치하지 않습니다.";

    /**
     * 검증 그룹 지정 (Bean Validation 스펙 필수 속성)
     * 특정 상황에서만 검증하고 싶을 때 사용
     */
    Class<?>[] groups() default {};

    /**
     * 메타데이터 전달용 (Bean Validation 스펙 필수 속성)
     * 실무에서 거의 사용 안 함
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 비밀번호 필드명 (기본값: "password")
     */
    String password() default "password";

    /**
     * 비밀번호 확인 필드명 (기본값: "passwordConfirm")
     */
    String passwordConfirm() default "passwordConfirm";
}