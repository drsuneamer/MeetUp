package com.meetup.backend.config.security;

import com.meetup.backend.jwt.AuthAccessDeniedHandler;
import com.meetup.backend.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * created by seongmin on 2022/10/20
 * updated by seongmin on 2022/11/01
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthAccessDeniedHandler authAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().and().csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .accessDeniedHandler(authAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/user/login/**", "/admin/signup/**", "/admin/login/**", "/swagger*/**", "/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .antMatchers("/admin", "/admin/signup").hasRole("Admin")
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();
    }
}
