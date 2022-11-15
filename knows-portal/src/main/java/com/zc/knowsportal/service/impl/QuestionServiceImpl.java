package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Question;
import com.zc.knowsportal.mapper.QuestionMapper;
import com.zc.knowsportal.model.Tag;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.service.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
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
}
