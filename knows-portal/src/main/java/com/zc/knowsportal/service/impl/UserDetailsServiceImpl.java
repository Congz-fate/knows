package com.zc.knowsportal.service.impl;

import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Permission;
import com.zc.knowsportal.model.Role;
import com.zc.knowsportal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Cong
 * @ClassName UserDetailsServiceImpl
 * @Description 安全登录实现类
 * @Date 15/11/2022  下午 5:51
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    // 这个方法会由Spring-Security框架内部自动调用
    // 在登录时,Spring-Security框架会将用户登录输入的用户名,当做参数调用这个方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户查询用户
        User user=userMapper.findUserByUsername(username);
        //2. 判断用户是否为空
        if(user==null){
            return null;
        }
        //3. 根据用户id查询所有权限
        List<Permission> permissions=userMapper
                .findUserPermissionsById(user.getId());
        //4. 将权限名称赋值到一个String[]中
        String[] auth=new String[permissions.size()];
        int i=0;
        for(Permission p : permissions){
            auth[i]=p.getName();
            i++;
        }
        // 按id查询所有角色
        List<Role> roles=userMapper.findUserRolesById(user.getId());
        // 因为要将角色名称也保存到auth中,所以auth要扩容
        auth = Arrays.copyOf(auth,auth.length+roles.size());
        for(Role r:roles){
            auth[i]=r.getName();
            i++;
        }
        //5. 根据现有数据构建UserDetails类型对象
        UserDetails details= org.springframework.security
                .core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                //账户是否锁定,true锁,false不锁
                .accountLocked(user.getLocked()==1)
                //账户是否可用,true不可用,false可用
                .disabled(user.getEnabled()==0)
                .build();
        //6. 返回UserDetails类型对象
        // 千万别忘了返回!!!!!
        return details;
    }
}
