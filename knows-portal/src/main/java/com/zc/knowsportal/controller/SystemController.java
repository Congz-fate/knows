package com.zc.knowsportal.controller;

import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.service.impl.UserServiceImpl;
import com.zc.knowsportal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Cong
 * @ClassName SystemController
 * @Description 注册类
 * @Date 15/11/2022  下午 7:52
 */
@RestController
//下面的注解是lombok提供的
// 这个注解的作用是会自动在当前类中声明一个log对象,该对象可以记录日志
@Slf4j
public class SystemController {

    @Autowired
    private UserServiceImpl userService ;

    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo,BindingResult result){
            log.debug("接收到注册信息:{}",registerVo);
            //判读验证后是否有错误
            if(result.hasErrors()){
                // 接收错误信息
                String msg=result.getFieldError().getDefaultMessage();
                // 返回错误信息
                return msg;
            }
            //控制层调用业务逻辑层对象的方法
            try {
                userService.registerStudent(registerVo);
                return "测试完成";
            }catch (ServiceException e){
                log.error("注册失败!",e);
                return e.getMessage();
            }
    }
}
