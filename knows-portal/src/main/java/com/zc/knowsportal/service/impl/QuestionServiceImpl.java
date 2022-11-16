package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.mapper.*;
import com.zc.knowsportal.model.*;
import com.zc.knowsportal.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.service.ITagService;
import com.zc.knowsportal.service.IUserService;
import com.zc.knowsportal.vo.QuestionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类(问题实现
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;
    @Autowired
    private IUserService userService;

    @Override
    public PageInfo<Question> getMyQuestions(String username, Integer pageNum, Integer pageSize) {
            User user=userMapper.findUserByUsername(username);
            QueryWrapper<Question> query=new QueryWrapper<>();
            query.eq("user_id",user.getId());
            query.eq("delete_status",0);
            query.orderByDesc("createtime");
            // 在执行查询之前,设置分页操作
            // startPage([页码],[每页条数])
            PageHelper.startPage(pageNum,pageSize);
            List<Question> list=questionMapper.selectList(query);
            //上面查询出的问题集合中的每个问题都没有对应的标签
            // 遍历这个集合,把每个问题的标签集合都赋值
            for(Question question:list){
                List<Tag> tags=tagNames2Tags(question.getTagNames());
                question.setTags(tags);
            }
            log.debug("当前用户问题总数:{}",list.size());
            return new PageInfo<>(list);
    }
    @Autowired
    private ITagService tagService;

    // 根据标签名称字符串(tag_names)返回对应List的方法
    public List<Tag> tagNames2Tags(String tagNames){
        // tagNames:"Java基础,java SE,面试题"
        String[] names= tagNames.split(",");
        // names:{"Java基础","java SE","面试题"}
        // 声明一个空List用户保存结果
        List<Tag>  tags=new ArrayList<>();
        // 获得包含全部标签的Map
        Map<String,Tag> tagMap=tagService.getTagMap();
        // 循环遍历names
        for(String name:names){
            //根据标签名称获得标签对象
            Tag tag=tagMap.get(name);
            // 将对应的标签对象保存到循环前声明的List中
            tags.add(tag);
        }
        // 千万别忘了返回!!!!
        return tags;
    }

    @Override
    public void saveQuestion(QuestionVo questionVo, String username) {
        //1. 根据用户名查询用户信息
        User user=userMapper.findUserByUsername(username);
        //2. 根据用户选择的标签,拼接tag_names数据
        StringBuilder builder=new StringBuilder();
        for(String tagName : questionVo.getTagNames()){
            builder.append(tagName).append(",");
        }
        //{"java基础","javaSE","面试题"}
        //java基础,javaSE,面试题
        //kjsdflk,
        //01234567
        String tagNames=builder
                .deleteCharAt(builder.length()-1).toString();
        //3. 执行question的赋值与新增
        Question question=new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);
        int num=questionMapper.insert(question);
        if(num!=1){
            throw new ServiceException("数据库忙");
        }
        //4. 新增question与tag的关系
        //  获得包含所有标签的map
        Map<String,Tag> tagMap=tagService.getTagMap();
        // 循环遍历用户选中的所有标签
        for(String tagName : questionVo.getTagNames()){
            // 根据标签名称获得标签对象
            Tag t=tagMap.get(tagName);
            QuestionTag questionTag=new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(t.getId());
            // 执行关系对象的新增
            num=questionTagMapper.insert(questionTag);
            if(num!=1){
                throw new ServiceException("数据库异常");
            }
            log.debug("新增了关系:{}",questionTag);
        }

        //5. 新增question与user(讲师)的关系
        Map<String,User> teacherMap=userService.getTeacherMap();
        for(String teacherName: questionVo.getTeacherNicknames()){
            User teacher=teacherMap.get(teacherName);
            UserQuestion userQuestion=new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(teacher.getId())
                    .setCreatetime(LocalDateTime.now());
            num=userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw new ServiceException("数据库异常");
            }
            log.debug("新增了关联讲师:{}",userQuestion);
        }
    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {

        QueryWrapper<Question> query=new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        Integer count=questionMapper.selectCount(query);
        // 千万别忘了返回!!!!!
        return count;
    }

    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
        User user=userMapper.findUserByUsername(username);
        // 执行分页查询,先设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions=
                questionMapper.findTeacherQuestion(user.getId());
        // 遍历当前所有问题,将每个问题的tags集合赋值
        for(Question q: questions){
            List<Tag> tags=tagNames2Tags(q.getTagNames());
            q.setTags(tags);
        }
        // 返回PageInfo
        return new PageInfo<>(questions);
    }

    @Override
    public Question getQuestionById(Integer id) {
        // 根据问题id 查询问题对象
        Question question=questionMapper.selectById(id);
        // 根据当前问题的tagNames字符串获得tags集合
        List<Tag> tags=tagNames2Tags(question.getTagNames());
        question.setTags(tags);
        // 千万别忘了返回
        return question;
    }

}
