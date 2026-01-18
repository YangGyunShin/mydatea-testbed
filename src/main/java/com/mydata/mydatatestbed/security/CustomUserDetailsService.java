package com.mydata.mydatatestbed.security;

import com.mydata.mydatatestbed.entity.Member;
import com.mydata.mydatatestbed.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security용 사용자 조회 서비스
 *
 * UserDetailsService 인터페이스:
 * - Spring Security가 로그인 시 사용자를 조회하는 표준 인터페이스
 * - loadUserByUsername() 메서드 하나만 구현하면 됨
 *
 * 로그인 처리 흐름:
 * 1. 사용자가 이메일/비밀번호 입력
 * 2. Spring Security가 이 Service의 loadUserByUsername() 호출
 * 3. DB에서 Member 조회
 * 4. CustomUserDetails로 감싸서 반환
 * 5. Spring Security가 비밀번호 비교 (PasswordEncoder 사용)
 * 6. 일치하면 로그인 성공, SecurityContext에 인증 정보 저장
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 사용자명(이메일)으로 사용자 정보 조회
     *
     * @param email 로그인 시 입력한 이메일 (username 파라미터명은 인터페이스 규약)
     * @return UserDetails 구현체 (CustomUserDetails)
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     *
     * Spring Security가 이 메서드를 호출하는 시점:
     * - formLogin에서 로그인 폼 제출 시
     * - HTTP Basic 인증 시
     * - @AuthenticationPrincipal로 현재 사용자 조회 시
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "회원을 찾을 수 없습니다: " + email
                ));

        return new CustomUserDetails(member);
    }
}

/*
## 실제 로그인 흐름 (전체)

1. 사용자가 이메일/비밀번호 입력 후 로그인 버튼 클릭

2. Spring Security가 CustomUserDetailsService.loadUserByUsername("이메일") 호출

3. CustomUserDetailsService가:
   - MemberRepository.findByEmail()로 DB 조회
   - 찾으면 new CustomUserDetails(member) 반환
   - 못 찾으면 UsernameNotFoundException 던짐

4. Spring Security가 반환받은 CustomUserDetails에서:
   - getPassword()로 저장된 비밀번호 가져옴
   - PasswordEncoder.matches(입력값, 저장값)으로 비교
   - isEnabled()로 계정 활성화 여부 확인

5. 모든 검증 통과 시:
   - SecurityContext에 CustomUserDetails 저장
   - 이후 @AuthenticationPrincipal로 현재 로그인 사용자 조회 가능
*/