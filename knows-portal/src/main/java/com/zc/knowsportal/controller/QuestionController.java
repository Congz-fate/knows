package com.zc.knowsportal.controller;


import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.model.Question;
import com.zc.knowsportal.service.IQuestionService;
import com.zc.knowsportal.vo.QuestionVo;
import com.zc.knowsportal.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器（问题
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Slf4j
@RestController
@RequestMapping("/v1/questions")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    // localhost:8080/v1/questions/my

    /**
     * 这个方法响应当前登录用户的所有问题列表
     * @param user
     * @param pageNum
     * @return
     */
    @GetMapping("/my")
    public PageInfo<Question> my(
        //下面的注解功能是从Spring-Security中获得当前登录用户
        // UserDetails中包含用户名和用户的所有权限
        @AuthenticationPrincipal UserDetails user, Integer pageNum) {
        UserVo userVo = new UserVo();
        if (pageNum == null) {
            pageNum = 1;
        }
        int pageSize = 6;
        //调用业务逻辑层的方法
        PageInfo<Question> pageInfo =
                questionService.getMyQuestions(user.getUsername(), pageNum, pageSize);
        return pageInfo;
    }

    /**
     * 新增问题(学生发布问题)的控制层方法
     * @param questionVo
     * @param result
     * @param user
     * @return
     */
    @PostMapping("")
    public String createQuestion(
            @Validated QuestionVo questionVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user) {
        log.debug("获得表单信息:{}",questionVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            return msg;
        }
        try {
            questionService.saveQuestion(
                    questionVo, user.getUsername());
            return "ok";
        }catch (ServiceException e){
            log.error("发生业务异常",e);
            return e.getMessage();
        }
    }

}
