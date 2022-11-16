package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Answer;
import com.zc.knowsportal.mapper.AnswerMapper;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.IAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Override
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        User user=userMapper.findUserByUsername(username);
        Answer answer=new Answer()
                .setContent(answerVo.getContent())
                .setLikeCount(0)
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setQuestId(answerVo.getQuestionId())
                .setCreatetime(LocalDateTime.now())
                .setAcceptStatus(0);
        int num= answerMapper.insert(answer);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        // 千万别忘了返回answer!!!
        return answer;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Integer questionId) {
        // 调用关联查询方法
        // 直接查询出所有回答和每个回答包含的评论
        List<Answer> answers=answerMapper.findAnswersByQuestionId(questionId);
        // 千万别忘了返回
        return answers;
    }
}
