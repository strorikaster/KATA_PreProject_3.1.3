package com.webcrudsecurityboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@ComponentScan("com.webcrudsecurityboot")
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final LoginSuccessHandler loginSuccessHandler;
    private final EncoderConfig encoderConfig;

    @Autowired
    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, LoginSuccessHandler loginSuccessHandler, EncoderConfig encoderConfig) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.encoderConfig = encoderConfig;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoderConfig.passwordEncoder()); // конфигурация для прохождения аутентификации
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/logout").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().authenticated();

        httpSecurity.formLogin()
                //указываем логику обработки при логине
                .successHandler(loginSuccessHandler)
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("email")
                .passwordParameter("password")
                //укзываем форму логина
                .loginPage("/login")
                // даем доступ к форме логина всем
                .permitAll();

        httpSecurity.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //.logout()
                .logoutUrl("/logout")
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login");
        return httpSecurity.build();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
