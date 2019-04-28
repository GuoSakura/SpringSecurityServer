package com.guosh.security.browser.config;

import com.guosh.security.browser.authentication.BrowserAuthenticationFailureHandler;
import com.guosh.security.browser.authentication.BrowserAuthenticationSuccessHandler;
import com.guosh.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private BrowserAuthenticationSuccessHandler browserAuthenticationSuccessHandler;

    @Autowired
    private BrowserAuthenticationFailureHandler browserAuthenticationFailureHandler;

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//关闭跨站防护
                .authorizeRequests()//下面授权配置
                    .antMatchers("/login",securityProperties.getBrowser().getLoginPage()).permitAll()//login请求除外不需要认证
                    .anyRequest().authenticated()//所有请求都需要身份认证
                .and()
                    .formLogin() //表单登陆页
                    .loginPage("/login")//登陆页面
                    .loginProcessingUrl("/authentication/form")//自定义form表单登陆提交地址默认是/login
                    .successHandler(browserAuthenticationSuccessHandler)//登陆成功后返回json信息
                    .failureHandler(browserAuthenticationFailureHandler);//登陆失败返回json

    }
}