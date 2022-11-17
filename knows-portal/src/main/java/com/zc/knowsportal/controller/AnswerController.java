package com.zc.knowsportal.controller;


import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.model.Answer;
import com.zc.knowsportal.service.IAnswerService;
import com.zc.knowsportal.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器（回答控制器
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {
    @Autowired
    private IAnswerService answerService;

    @PostMapping("")
    //@PreAuthorize("hasAuthority('/question/answer')")
    //hasRole('TEACHER')这种写法会自动到当前登录用户的所有auth中寻找
    //         "ROLE_TEACHER"角色
    @PreAuthorize("hasRole('TEACHER')")
    public Answer postAnswer(
            @Validated AnswerVo answerVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user){
            log.debug("新增回答信息:{}",answerVo);
            if(result.hasErrors()){
                String msg=result.getFieldError().getDefaultMessage();
                throw new ServiceException(msg);

            }
            // 这里调用业务逻辑层方法后返回
            Answer answer=answerService.saveAnswer(
                    answerVo,user.getUsername());
            return answer;

    }

    @GetMapping("/question/{id}")
    public List<Answer> questionAnswers(
            @PathVariable Integer id){
        if(id==null){
            throw new ServiceException("必须提供问题id才能获得回答");
        }
        List<Answer> answers=answerService
                .getAnswersByQuestionId(id);
        return answers;
    }

    // 采纳回答的控制层方法
    @GetMapping("/{answerId}/solved")
    public String solved(
            @PathVariable Integer answerId,
            @AuthenticationPrincipal UserDetails user){
        boolean isSolved=answerService.accept(
                answerId,user.getUsername());
        if(isSolved){
            return "ok";
        }else{
            return "fail";
        }
    }
}
