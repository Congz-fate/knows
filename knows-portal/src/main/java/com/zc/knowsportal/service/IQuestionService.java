package com.zc.knowsportal.service;

import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.model.Tag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface IQuestionService extends IService<Question> {
    //  查询当前登录用户所有问题列表的方法
    PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize);

    List<Tag> tagNames2Tags(String tagNames);
}
