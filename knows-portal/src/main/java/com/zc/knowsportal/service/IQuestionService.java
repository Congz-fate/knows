package com.zc.knowsportal.service;

import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.model.Tag;
import com.zc.knowsportal.vo.QuestionVo;

import java.util.List;

/**
 * <p>
 *  服务类(问题
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface IQuestionService extends IService<Question> {
    /**
     * 查询当前登录用户所有问题列表的方法
     * @param username
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize);

    /**
     * 使用标签名字（tagName）查找
     * @param tagNames
     * @return
     */
    List<Tag> tagNames2Tags(String tagNames);

    /**
     *  增添问题
     * @param questionVo
     * @param username
     */
    void saveQuestion(QuestionVo questionVo, String username);

    /**
     * 根据用户id查询用户问题数
     * @param userId
     * @return
     */
    Integer countQuestionsByUserId(Integer userId);

}
