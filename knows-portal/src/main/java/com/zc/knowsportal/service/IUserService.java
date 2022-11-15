package com.zc.knowsportal.service;

import com.zc.knowsportal.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zc.knowsportal.vo.RegisterVo;

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
    // 注册学生的业务逻辑层方法
    void registerStudent(RegisterVo registerVo);

    // 查询所有讲师的业务逻辑层方法
    List<User> getTeachers();

    // 查询所有讲师Map的方法
    Map<String,User> getTeacherMap();
}
