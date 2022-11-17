package com.zc.knowsportal.mapper;

import com.zc.knowsportal.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* mapper 接口（问题
* @author zc.com
* @since 2022-11-15
*/
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询当前登录讲师任务列表
     * @param userId
     * @return
     */
    @Select("SELECT q.* FROM question q" +
            " LEFT JOIN user_question uq" +
            " ON uq.question_id=q.id" +
            " WHERE uq.user_id=#{userId} OR q.user_id=#{userId}"+
            " ORDER BY createtime desc")
    List<Question> findTeacherQuestion(Integer userId);

    /**
     * 按问题id修改问题的状态
     * @param questionId
     * @param status
     * @return
     */
    @Update("update question set status=#{status} " +
            " where id=#{questionId}")
    int updateStatus(@Param("questionId") Integer questionId,
                     @Param("status") Integer status);
}
