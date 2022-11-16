package com.zc.knowsportal.service;

import com.zc.knowsportal.model.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.vo.AnswerVo;

import java.util.List;

/**
 * <p>
 *  服务类(回复
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface IAnswerService extends IService<Answer> {
    /**
     * 新增回答的业务逻辑层方法:返回新增的回答对象
     * @param answerVo
     * @param username
     * @return
     */
    Answer saveAnswer(AnswerVo answerVo, String username);

    /**
     * 按问题id查询所有回答的业务逻辑层方法
     * @param questionId
     * @return
     */
    List<Answer> getAnswersByQuestionId(Integer questionId);
}
