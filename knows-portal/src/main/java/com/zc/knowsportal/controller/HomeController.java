package com.zc.knowsportal.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Cong
 * @ClassName HomeController
 * @Date 16/11/2022  下午 10:09
 * @Description 首页控制器
 */

@Controller
public class HomeController {
    // 区别（知识点）：
    //   RestController类中的方法返回字符串时,相当于响应给页面的信息
    //   Controller类中的方法返回字符串时,会解析为要跳转页面的路径

    /**
     * 声明讲师和学生角色的常量
     */
    public static final GrantedAuthority STUDENT=
            new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER=
            new SimpleGrantedAuthority("ROLE_TEACHER");

    //(value = {"/index.html","/"})表示
    // localhost:8080 和localhost:8080/index.html 都会访问这个控制器
    @GetMapping(value = {"/index.html","/"})
    public String index(
            @AuthenticationPrincipal UserDetails user){
        if(user.getAuthorities().contains(TEACHER)){
            // 将页面重定向到讲师首页
            return "redirect:/index_teacher.html";
        }else if(user.getAuthorities().contains(STUDENT)){
            return "redirect:/index_student.html";
        }
        return null;
    }
}
