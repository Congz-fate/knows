package com.zc.knowsportal.service;

import com.zc.knowsportal.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface ITagService extends IService<Tag> {
    // 查询所有标签的业务逻辑层方法
    List<Tag> getTags();
    // 查询包含所有标签的Map的业务逻辑层方法
    Map<String,Tag> getTagMap();
}
