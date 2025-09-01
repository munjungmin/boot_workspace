package com.sinse.customlogindb.security;

import com.sinse.customlogindb.model.member.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//스프링이 디폴트로 지원하는 폼로그인을 커스텀 하려면, 설정 클래스가 필요함
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MemberDetailsService memberDetailsService;

    public SecurityConfig(MemberDetailsService memberDetailsService) {
        this.memberDetailsService = memberDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //AuthenticationProvider 등록
    //개발자 대신 비밀번호를 비교 검증 (따라서 개발자는 Repository나 DAO등에서 password를 사용할 필요가 없다)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberDetailsService);  // 어떤 서비스를 이용할 지
        provider.setPasswordEncoder(passwordEncoder());     // 어떤 비밀번호 변환 객체를 이용할 지
        return provider;
    }

    //AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    //스프링 시큐리티의 처리를 담당하는 객체인 SecurityFilterChain 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/main").permitAll()
                        .anyRequest().authenticated())  // 모든 요청은 인증을 받아야 함(로그인 해야한다는 뜻)
                .formLogin(form -> form
                        .loginPage("/loginform")       //로그인 폼을 만나기 위한 요청 주소 등록
                        .loginProcessingUrl("/login")  //로그인 요청을 처리하는 주소
                        .usernameParameter("id")       //로그인 html 페이지의 form input의 name=id로 하겠다
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/main", true)  //로그인 성공시 main으로
                        .permitAll()
                );

        return http.build();
    }
}
