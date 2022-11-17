package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.mapper.QuestionMapper;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Answer;
import com.zc.knowsportal.mapper.AnswerMapper;
import com.zc.knowsportal.model.Question;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.IAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private QuestionMapper questionMapper ;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean accept(Integer answerId, String username) {
        // 获得登录用户身份信息
        User user=userMapper.findUserByUsername(username);
        // 获得当前采纳的回答对象
        Answer answer=answerMapper.selectById(answerId);
        // 查询当前问题对象
        Question question=questionMapper
                .selectById(answer.getQuestId());
        // 判断当前登录用户是这个问题的提问者
        // 注意判断条件开始有个! 是取反的
        if(!user.getId().equals(question.getUserId())){
            // 不是问题的提问者直接报异常
            throw new ServiceException("没有权限");
        }
        // 先修改answer表
        int num=answerMapper
                .updateAcceptStatus(answerId,1);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        // 再修改question表
        num=questionMapper.updateStatus(
                answer.getQuestId(),Question.SOLVED);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        //最后return true即可
        return true;
    }
}
