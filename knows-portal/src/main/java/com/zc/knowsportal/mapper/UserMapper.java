package com.zc.knowsportal.mapper;

import com.zc.knowsportal.model.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zc.knowsportal.vo.UserVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Mapper 接口
* @author zc.com
* @since 2022-11-15
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询出用户对象
     * @param username
     * @return
     */
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String  username);

    /**
     * 根据用户id查询所有权限
     * @param id
     * @return
     */
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

    /**
     * 根据username查找 UserVo
     * @param username
     * @return
     */
    @Select("select id,username,nickname from user " +
            " where username=#{username}")
    UserVo findUserVoByUsername(String username);

    /**
     * 问题数 可以根据用户的id查询
     * @return
     */
    @Select("SELECT COUNT(*) FROM question" +
            "WHERE user_id=#{id}")
    UserQuestion findQuestionByUserId();

    /**
     * 收藏数 可以根据用户的id查询
     * @return
     */
    @Select("SELECT COUNT(*) FROM user_collect" +
            "WHERE user_id=#{id}")
    UserCollect findCollectByUserId();

    /**
     * 根据用户id查询所有角色
     * @param userId
     * @return
     */
    @Select("SELECT r.id,r.name\n" +
            "FROM user u\n" +
            "LEFT JOIN user_role ur ON u.id=ur.user_id\n" +
            "LEFT JOIN role r ON ur.role_id=r.id\n" +
            "WHERE u.id=#{userId}")
    List<Role> findUserRolesById(Integer userId);

}
