package com.zc.knowsportal.mapper;

import com.zc.knowsportal.model.Permission;
import com.zc.knowsportal.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author zc.com
* @since 2022-11-15
*/
    @Repository
    public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询出用户对象
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String  username);

    // 根据用户id查询所有权限
    @Select("SELECT p.id , p.name" +
            " FROM" +
            " user u LEFT JOIN user_role ur" +
            " ON u.id=ur.user_id" +
            " LEFT JOIN role r  ON ur.role_id=r.id" +
            " LEFT JOIN role_permission rp" +
            " ON r.id=rp.role_id" +
            " LEFT JOIN permission p" +
            " ON rp.permission_id=p.id" +
            " WHERE u.id=#{id}")
    List<Permission> findUserPermissionsById(Integer id);
    }
