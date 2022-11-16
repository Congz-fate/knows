package com.zc.knowsportal.service;

import com.zc.knowsportal.model.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类(收藏
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
public interface IUserCollectService extends IService<UserCollect> {

    /**
     * 根据用户id查询用户收藏数
     * @param userId
     * @return
     */
    Integer countCollectByUserId(Integer userId);
}
