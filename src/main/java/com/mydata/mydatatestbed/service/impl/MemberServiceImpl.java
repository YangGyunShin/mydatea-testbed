package com.mydata.mydatatestbed.service.impl;

import com.mydata.mydatatestbed.dto.member.MemberResponseDto;
import com.mydata.mydatatestbed.dto.member.MemberSignupRequestDto;
import com.mydata.mydatatestbed.entity.Member;
import com.mydata.mydatatestbed.mapper.MemberMapper;
import com.mydata.mydatatestbed.repository.MemberRepository;
import com.mydata.mydatatestbed.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Member Service 구현체
 *
 * 비밀번호 일치 검증이 제거된 이유:
 * - @PasswordMatching 어노테이션이 Controller의 @Valid에서 자동 처리
 * - Service는 이미 검증 통과한 데이터만 받음
 * - Service는 비즈니스 로직에만 집중
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     *
     * 변경 사항:
     * - 비밀번호 일치 검증 코드 제거 (어노테이션으로 대체)
     * - Service는 비즈니스 로직만 담당
     */
    @Override
    @Transactional
    public MemberResponseDto signup(MemberSignupRequestDto requestDto) {
        // 1. 이메일 중복 확인
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. Entity 생성
        Member member = memberMapper.toEntity(requestDto, encodedPassword);

        // 4. 저장
        Member savedMember = memberRepository.save(member);

        // 5. 응답 DTO 반환
        return memberMapper.toResponseDto(savedMember);
    }

    @Override
    public Optional<MemberResponseDto> findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(memberMapper::toResponseDto);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void verifyEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        member.verifyEmail();
    }
}