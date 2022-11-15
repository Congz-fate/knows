package com.zc.knowsportal.service.impl;

import com.zc.knowsportal.model.Tag;
import com.zc.knowsportal.mapper.TagMapper;
import com.zc.knowsportal.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    //声明一个List来保存从数据库中查询的标签
    private List<Tag> tags=new CopyOnWriteArrayList<>();
    // 声明一个Map保存所有标签
    private Map<String,Tag> tagMap=new ConcurrentHashMap<>();
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getTags() {
        //      3
        // 这个if的主要功能是使除第一个人之外的
        //              所有用户不用获得锁就能访问所有标签提高运行速度
        if(tags.isEmpty()) {
            //        1   2
            // 防止多个用户(线程)同时向tags属性中新增多有标签
            synchronized (tags) {
                // 判断当前获得锁的线程运行之前是否已经对tags属性赋值
                if(tags.isEmpty()) {
                    // 查询出所有标签,查询结果的集合类型不是线程安全的
                    // 不能直接赋值给tags属性
                    List<Tag> tags = tagMapper.selectList(null);
                    // 使用addAll方法,将全部标签保存到线程安全的tags属性中
                    this.tags.addAll(tags);
                    for (Tag t:tags){
                        tagMap.put(t.getName(),t);
                    }
                    log.debug("缓存保存完成!");
                }
            }
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
        if(tagMap.isEmpty()){
            getTags();
        }
        return tagMap;
    }
}
