package com.mydata.mydatatestbed.mapper;

import com.mydata.mydatatestbed.dto.member.MemberResponseDto;
import com.mydata.mydatatestbed.dto.member.MemberSignupRequestDto;
import com.mydata.mydatatestbed.entity.Member;
import com.mydata.mydatatestbed.vo.EmailVo;
import com.mydata.mydatatestbed.vo.PasswordVo;
import com.mydata.mydatatestbed.vo.PhoneVo;
import org.springframework.stereotype.Component;

/**
 * Member 도메인 Mapper
 *
 * Mapper의 역할:
 * - DTO → Entity 변환 (요청 데이터를 도메인 객체로)
 * - Entity → DTO 변환 (도메인 객체를 응답 데이터로)
 *
 * 왜 별도 클래스로 분리하나?
 * 1. 단일 책임 원칙(SRP): 변환 로직만 담당
 * 2. 테스트 용이성: Mapper만 단위 테스트 가능
 * 3. 재사용성: 여러 Service에서 동일한 변환 로직 사용
 * 4. Entity/DTO 변경 시 영향 범위 최소화
 *
 * of(), from() 같은 정적 팩토리 메서드 대신 인스턴스 메서드를 사용하는 이유:
 * - 코딩 컨벤션: 정적 팩토리 메서드 사용 금지
 * - Spring Bean으로 등록하여 의존성 주입 가능 (나중에 다른 의존성 필요 시 유연함)
 */
@Component
public class MemberMapper {

    /**
     * 회원가입 요청 DTO → Member Entity 변환
     *
     * @param dto 회원가입 요청 데이터
     * @param encodedPassword 인코딩된 비밀번호 (Service에서 인코딩 후 전달)
     * @return Member Entity
     *
     * 비밀번호를 별도 파라미터로 받는 이유:
     * - DTO에는 평문 비밀번호가 있음
     * - 비밀번호 인코딩은 Service의 책임 (PasswordEncoder 의존)
     * - Mapper는 순수 변환만 담당, 외부 의존성 최소화
     */
    public Member toEntity(MemberSignupRequestDto dto, String encodedPassword) {
        return Member.builder()
                .email(EmailVo.builder()
                        .value(dto.getEmail())
                        .build())
                .password(PasswordVo.builder()
                        .value(encodedPassword)
                        .build())
                .name(dto.getName())
                .phone(dto.getPhone() != null ?
                        PhoneVo.builder()
                                .value(dto.getPhone())
                                .build()
                        : null)
                .company(dto.getCompany())
                .department(dto.getDepartment())
                .build();
    }

    /**
     * Member Entity → 응답 DTO 변환
     *
     * @param member Member Entity
     * @return 회원 응답 DTO
     *
     * VO에서 값을 꺼낼 때 null 체크하는 이유:
     * - phone은 nullable 필드
     * - null인 VO에서 getValue() 호출 시 NullPointerException 발생
     */
    public MemberResponseDto toResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail().getValue())
                .name(member.getName())
                .phone(member.getPhone() != null ? member.getPhone().getValue() : null)
                .company(member.getCompany())
                .department(member.getDepartment())
                .role(member.getRole().getDisplayName())
                .emailVerified(member.isEmailVerified())
                .phoneVerified(member.isPhoneVerified())
                .createdAt(member.getCreatedAt())
                .lastLoginAt(member.getLastLoginAt())
                .build();
    }
}