package com.sinse.demojwt.security;

import com.sinse.demojwt.model.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class FormLoginSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public FormLoginSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/main").permitAll()    // /main url에 대한 모든 사용자의 접근 허용
                        .anyRequest().authenticated()               // 그 외에 다른 요청은 인증된 사용자만 접근 허용
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 인증되지 않았을때 리다이렉트될 로그인 페이지 url
                        .loginProcessingUrl("/login")  // 로그인 요청(post) 처리 url
                        .defaultSuccessUrl("/auth", true)  //로그인 성공 후 이동할 기본 url, 인증 성공 후 항상 이 url로 리다이렉트하는지 여부
                        .permitAll()  //폼로그인 관련된 모든 url (로그인 페이지, 처리 url)에 대해 모든 사용자의 접근 허용
                )
                .build();
    }

    // AuthenticationManager (interface, 구현체로 ProviderManager 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //AuthenticationProvider interface - 구현체 DaoAuthenticationProvider (인증 종류마다 구현체 존재 Jwt, OAuth..)
    // UserDetailService(구현체를 선택하거나 직접 구현하여 user 정보를 가져올 저장소를 선택할 수 있다)와 PasswordEncoder를 사용
    // UserDetailService는 말그대로 Service 계층이라 보안 관련 설정 파일에서 @Bean으로 생성하기보다 @Service로 스캔한다.
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 원하는 password 암호화를 구현한 객체 선택
    // BCrypt - 솔트를 내부에서 생성하고, 일부러 연산 속도를 늦춰 브루트포스에 대응함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
