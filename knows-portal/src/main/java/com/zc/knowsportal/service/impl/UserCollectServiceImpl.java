package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zc.knowsportal.model.UserCollect;
import com.zc.knowsportal.mapper.UserCollectMapper;
import com.zc.knowsportal.service.IUserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {


    @Autowired
    private UserCollectMapper userCollectMapper ;

    @Override
    public Integer countCollectByUserId(Integer userId) {
        QueryWrapper<UserCollect> query=new QueryWrapper<>();
        query.eq("user_id",userId);
        Integer count=userCollectMapper.selectCount(query);
        // 千万别忘了返回!!!!!
        return count;
    }
}
