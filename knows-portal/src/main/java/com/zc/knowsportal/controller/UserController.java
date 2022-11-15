package com.zc.knowsportal.controller;


import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {

    // 一般用户登录之后,可以访问当前网站的所有资源
// 但是如果一个控制方法表标明了一个特殊的权限,那么这个用户必须有这个权限才能访问
    @GetMapping("/answer")
// 下面注解的含义就是必须包含"abc"这个资格,才能访问下面的方法
    @PreAuthorize("hasAuthority('abc')")
    public String answer(){
        return "可以回答问题";
    }
    @GetMapping("/close")
    @PreAuthorize("hasAuthority('xyz')")
    public String close(){
        return "可以关闭网站";
    }

    @Autowired
    private IUserService userService;

    @GetMapping("/master")
    public List<User> master(){
        List<User> users=userService.getTeachers();
        return users;
    }
}
