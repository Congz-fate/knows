package com.zc.knowsportal.mapper;

import com.zc.knowsportal.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口(
    * </p>
*
* @author zc.com
* @since 2022-11-15
*/
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {
    /**
     * 对应AnswerMapper.xml文件中的id名称编写方法
     * @param questionId
     * @return
     */
    List<Answer> findAnswersByQuestionId(Integer questionId);
}
