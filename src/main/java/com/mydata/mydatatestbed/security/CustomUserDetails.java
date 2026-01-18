package com.mydata.mydatatestbed.security;

import com.mydata.mydatatestbed.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security용 사용자 정보 래퍼
 *
 * UserDetails 인터페이스:
 * - Spring Security가 인증/인가에 사용하는 표준 인터페이스
 * - 사용자명, 비밀번호, 권한, 계정 상태 등을 제공
 *
 * 왜 직접 구현하나?
 * - 우리 Member Entity를 Spring Security가 이해할 수 있게 변환
 * - Member Entity 자체가 UserDetails를 구현하면 Entity에 Security 의존성이 생김
 * - 별도 클래스로 분리하여 관심사 분리 (Member는 도메인, CustomUserDetails는 인프라)
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    /**
     * 사용자 권한 목록 반환
     *
     * GrantedAuthority: Spring Security의 권한 표현 인터페이스
     * SimpleGrantedAuthority: 문자열 기반 권한 구현체
     *
     * "ROLE_" 접두사:
     * - Spring Security 규약
     * - hasRole("USER") 체크 시 "ROLE_USER"를 찾음
     * - 우리 MemberRole Enum에 이미 "ROLE_" 접두사 포함
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().name())
        );
    }

    /**
     * 인증에 사용할 비밀번호
     * VO에서 실제 값을 꺼내서 반환
     */
    @Override
    public String getPassword() {
        return member.getPassword().getValue();
    }

    /**
     * 인증에 사용할 사용자명 (우리는 이메일 사용)
     * VO에서 실제 값을 꺼내서 반환
     */
    @Override
    public String getUsername() {
        return member.getEmail().getValue();
    }

    /**
     * 계정 만료 여부
     * true: 만료되지 않음 (정상)
     *
     * 만료 기능이 필요 없으면 항상 true 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * true: 잠기지 않음 (정상)
     *
     * 로그인 실패 횟수 초과 시 잠금 기능 구현 시 활용
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명(비밀번호) 만료 여부
     * true: 만료되지 않음 (정상)
     *
     * 비밀번호 주기적 변경 강제 시 활용
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     *
     * 이메일 인증 여부로 판단:
     * - true: 활성화됨 (로그인 가능)
     * - false: 비활성화됨 (로그인 불가)
     *
     * 이메일 미인증 사용자는 로그인할 수 없음
     */
    @Override
    public boolean isEnabled() {
        return member.isEmailVerified();
    }
}