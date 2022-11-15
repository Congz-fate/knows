package com.zc.knowsportal.security;

import com.zc.knowsportal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author Cong
 * @ClassName SecurityConfig
 * @Description 安全登录配置类
 * @Date 15/11/2022  下午 5:55
 */
//表示当前类是SpringBoot的配置类
@Configuration
//启动Spring-Security提供的权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.authorizeRequests() // 对请求进行授权设置
                .antMatchers(
                        "/index_student.html",
                        "/js/*",
                        "/css/*",
                        "/img/**",
                        "/bower_components/**",
                        "/login.html",
                        "/register.html",
                        "/register",
                        "/v1/**"
                )       // 设置匹配路径
                .permitAll()                //上述路径全部允许访问,无需登录
                .anyRequest()               // 其他请求
                .authenticated()            //需要登录才能访问
                .and().formLogin()          // 登录使用表单
                .loginPage("/login.html")   // 设置登录页面的路径
                .loginProcessingUrl("/login")//处理登录的路径
                .defaultSuccessUrl("/index_student.html")
                //默认情况下,登录成功跳转的页面
                .failureUrl("/login.html?error")
                // 如果登录失败,显示登录页面提示登录失败
                .and().logout()             // 开始配置登出
                .logoutUrl("/logout")       // 设置登出路径
                .logoutSuccessUrl("/login.html?logout")
                // 设置登出成功显示登录页面,提示登出成功
                .and().csrf().disable();    // 后面解释
    }
}
