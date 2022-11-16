package com.zc.knowsportal.service;

import com.zc.knowsportal.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.vo.RegisterVo;
import com.zc.knowsportal.vo.UserVo;

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
public interface IUserService extends IService<User> {
    /**
     * 注册学生的业务逻辑层方法
     * @param registerVo
     */
    void registerStudent(RegisterVo registerVo);

    /**
     * 查询所有讲师的业务逻辑层方法
     * @return
     */
    List<User> getTeachers();

    /**
     * 查询所有讲师Map的方法
     * @return
     */
    Map<String,User> getTeacherMap();

    /**
     * 根据用户名查询用户信息面板
     * @param username
     * @return
     */
    UserVo getUserVo(String username);
}
