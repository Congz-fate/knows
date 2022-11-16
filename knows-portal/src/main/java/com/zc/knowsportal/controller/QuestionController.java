package com.zc.knowsportal.controller;


import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.model.Question;
import com.zc.knowsportal.service.IQuestionService;
import com.zc.knowsportal.vo.QuestionVo;
import com.zc.knowsportal.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
            pageNum = 1 ;
        }
        int pageSize =6 ;
        if (user!=null){
            //调用业务逻辑层的方法
            PageInfo<Question> pageInfo =
                    questionService.getMyQuestions(user.getUsername(), pageNum, pageSize);
            return pageInfo;
        }else {
            return new PageInfo<>();
        }
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

    /**
     * 查询当前登录讲师的任务列表
     * 当前方法需要拥有回答权限的讲师才能访问
     * @param user
     * @param pageNum
     * @return
     */
    @GetMapping("/teacher")
    @PreAuthorize("hasAuthority('/question/answer')")
    public PageInfo<Question> teacher(
            @AuthenticationPrincipal UserDetails user,
            Integer pageNum){
        if(pageNum==null){
            pageNum=1;
        }
        int pageSize=6;
        PageInfo<Question> pageInfo=questionService
                .getTeacherQuestions(user.getUsername(),
                        pageNum,pageSize);
        // 千万别忘了返回!!!
        return pageInfo;
    }

    /**
     * 控制器路径使用{},可以用作占位符 若未指定则自动匹配占位符
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Question question(
            @PathVariable Integer id){
        // 要想在参数中获得匹配的数据
        //  1.必须使用@PathVariable 注解标明
        //  2.参数名称必须和{}里的名字对应
        Question question=questionService.getQuestionById(id);
        return question;
    }

}
