package com.zc.knowsportal.mapper;

import com.zc.knowsportal.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
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
    /**
     *  根据回答id修改采纳状态
     *  JVM默认情况下是不会保存局部变量的名称的,方法参数就是局部变量,
     *      所以运行时,JVM不知道方法参数的名称是什么
     *  SpringBoot官方脚手架对这一设置进行了修改,
     *      使JVM运行时保存住了方法参数的名称,
     *      所以我们的项目如果是官方脚手架创建的,是可以正常运行的
     *  但是国内常用的阿里脚手架没有修改这个设置,JVM仍然不保存变量名
     *      如果是这样,就可以在参数之前添加@Param注解
     *      在这个注解中声明对应的参数名称即可
     */
    @Update("update answer set accept_status=#{acceptStatus} " +
            " where id=#{answerId}")
    int updateAcceptStatus(@Param("answerId") Integer answerId,
                           @Param("acceptStatus") Integer acceptStatus);
}
