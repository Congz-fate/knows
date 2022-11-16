package com.zc.knowsportal.controller;


import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.model.Comment;
import com.zc.knowsportal.service.ICommentService;
import com.zc.knowsportal.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器(评论
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping
    public Comment postComment(
            @Validated CommentVo commentVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user){
        log.debug("接收到表单信息:{}",commentVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        //调用业务逻辑层
        Comment comment=commentService
                .saveComment(commentVo,user.getUsername());
        // 千万别忘了返回Comment!!!
        return comment;
    }

    // localhost:8080/v1/comments/20/delete

    /**
     * 删除评论的控制器方法
     * @param id
     * @param user
     * @return
     */
    @GetMapping("/{id}/delete")
    public String removeComment(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails user){
        boolean isDelete= commentService
                .removeComment(id,user.getUsername());
        if(isDelete){
            return "ok";
        }else{
            return "数据库异常或没有这个评论";
        }
    }
    // 修改评论的控制器方法
    @PostMapping("/{id}/update")
    public Comment updateComment(
            @PathVariable Integer id,
            @Validated CommentVo commentVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user){
        log.debug("修改的信息为:{}",commentVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        // 调用业务逻辑层
        Comment comment=commentService.updateComment(
                id,commentVo,user.getUsername());
        // 千万别忘了返回comment
        return comment;
    }
}
