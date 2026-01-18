package com.mydata.mydatatestbed.dto.member;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원 응답 DTO
 *
 * @Getter + @Builder만으로 충분한 이유:
 * - ResponseDto는 Mapper에서 Builder로만 생성함
 * - Spring MVC 바인딩 안 함 → @NoArgsConstructor 불필요
 * - 외부에서 모든 필드 생성자 호출 안 함 → @AllArgsConstructor 불필요
 *
 * final 필드를 안 쓰는 이유:
 * - @Builder만 쓰면 Lombok이 내부적으로 @AllArgsConstructor 자동 생성
 * - final 필드가 있으면 기본 생성자 관련 충돌 발생 가능
 * - 어차피 Setter가 없으므로 사실상 불변
 */
@Getter
@Builder
public class MemberResponseDto {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private String company;
    private String department;
    private String role;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}