package com.zc.knowsportal.service;

import com.zc.knowsportal.model.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.vo.CommentVo;

/**
 * <p>
 *  服务类 （评论
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 新增一条评论的方法
     * @param commentVo
     * @param username
     * @return
     */
    Comment saveComment(CommentVo commentVo, String username);

    /**
     * 按id删除评论的方法(需要验证当前用户的是否能够删除)
     * @param commentId
     * @param username
     * @return
     */
    boolean removeComment(Integer commentId,String username);

    /**
     * 按id修改评论的方法(需要验证当前用户的是否能够修改)
     * @param commentId
     * @param commentVo
     * @param username
     * @return
     */
    Comment updateComment(Integer commentId,
                          CommentVo commentVo, String username);
}
