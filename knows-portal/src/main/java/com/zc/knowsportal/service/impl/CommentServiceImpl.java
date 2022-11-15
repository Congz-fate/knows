package com.zc.knowsportal.service.impl;

import com.zc.knowsportal.model.Comment;
import com.zc.knowsportal.mapper.CommentMapper;
import com.zc.knowsportal.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
