package com.zc.knowsportal.service.impl;

import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Comment;
import com.zc.knowsportal.mapper.CommentMapper;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Comment saveComment(CommentVo commentVo, String username) {
        User user=userMapper.findUserByUsername(username);
        Comment comment=new Comment()
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setCreatetime(LocalDateTime.now());
        int num=commentMapper.insert(comment);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        //千万别忘了返回comment!!!!
        return comment;
    }

    @Override
    public boolean removeComment(Integer commentId, String username) {
        // 获得用户信息
        User user=userMapper.findUserByUsername(username);
        // 判断是不是讲师,如果是讲师,无条理删除评论
        if(user.getType().equals(1)){
            int num=commentMapper.deleteById(commentId);
            return num==1;
        }
        // 如果不是讲师,要判断登录用户是不是评论的发布者
        // 所以要先查询评论的详情
        Comment comment=commentMapper.selectById(commentId);
        if(comment.getUserId().equals(user.getId())){
            int num=commentMapper.deleteById(commentId);
            return num==1;
        }

        throw new ServiceException("权限不足");
    }

    @Override
    public Comment updateComment(Integer commentId, CommentVo commentVo, String username) {
        // 获得用户信息
        User user=userMapper.findUserByUsername(username);
        // 查询要修改的评论的对象
        Comment comment=commentMapper.selectById(commentId);
        // 是讲师或评论的发布者就可以修改该评论
        if(user.getType().equals(1)||
                comment.getUserId().equals(user.getId())){
            // 修改comment的内容
            comment.setContent(commentVo.getContent());
            // 按comment对象现在的值对数据库进行修改
            int num=commentMapper.updateById(comment);
            if(num!=1){
                throw new ServiceException("数据库异常");
            }
            return comment;
        }
        // 如果没有进入if直接抛出权限不足的异常提示
        throw new ServiceException("权限不足");
    }
}
