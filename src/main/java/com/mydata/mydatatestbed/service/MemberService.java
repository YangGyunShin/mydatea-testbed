package com.mydata.mydatatestbed.service;

import com.mydata.mydatatestbed.dto.member.MemberResponseDto;
import com.mydata.mydatatestbed.dto.member.MemberSignupRequestDto;

import java.util.Optional;

/**
 * Member Service 인터페이스
 *
 * 인터페이스를 분리하는 이유:
 * 1. 의존성 역전 원칙(DIP): Controller는 구현체가 아닌 인터페이스에 의존
 * 2. 테스트 용이성: Mock 객체로 쉽게 대체 가능
 * 3. 유연성: 구현체 교체가 자유로움 (예: MemberServiceImpl → MemberServiceV2Impl)
 *
 * 클린 아키텍처 관점:
 * - 이 인터페이스는 Application Layer(Use Case)에 해당
 * - "회원가입", "회원 조회" 같은 유스케이스를 정의
 */
public interface MemberService {

    /**
     * 회원가입 처리
     *
     * @param requestDto 회원가입 요청 데이터
     * @return 생성된 회원 응답 DTO
     */
    MemberResponseDto signup(MemberSignupRequestDto requestDto);

    /**
     * 이메일로 회원 조회
     *
     * @param email 조회할 이메일
     * @return 회원 응답 DTO (없으면 Optional.empty())
     */
    Optional<MemberResponseDto> findByEmail(String email);

    /**
     * 이메일 중복 확인
     *
     * @param email 확인할 이메일
     * @return 중복 여부 (true: 이미 존재함)
     */
    boolean isEmailDuplicate(String email);

    /**
     * 이메일 인증 처리
     *
     * @param email 인증할 회원 이메일
     */
    void verifyEmail(String email);
}