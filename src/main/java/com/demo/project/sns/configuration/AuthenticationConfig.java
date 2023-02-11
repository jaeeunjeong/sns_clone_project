package com.demo.project.sns.configuration;

import com.demo.project.sns.configuration.filter.JwtTokenFilter;
import com.demo.project.sns.exception.CustomAuthenticationEntryPoint;
import com.demo.project.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String key;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // http request가 들어왔을 때에 대한 시큐리티 설정
                .authorizeRequests()
                .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 요청 때마다 토큰이 어떤 유저를 가리키는 지에 대해 확인하는 필터
                .and()
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                // error 발생 후 이벤트핸들링
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint)
        ;
    }
}
