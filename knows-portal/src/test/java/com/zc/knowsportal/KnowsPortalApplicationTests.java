package com.zc.knowsportal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zc.knowsportal.mapper.ClassroomMapper;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.Classroom;
import com.zc.knowsportal.model.Permission;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.service.IUserService;
import com.zc.knowsportal.vo.RegisterVo;
import com.zc.knowsportal.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KnowsPortalApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    UserMapper userMapper;
    @Test
    public void userPermission(){
        User user=userMapper.findUserByUsername("tc2");
        System.out.println(user);
        List<Permission> permissions=userMapper
                .findUserPermissionsById(user.getId());
        for(Permission p:permissions){
            System.out.println(p);
        }
    }
    @Autowired
    ClassroomMapper classroomMapper;
    @Test
    public void query(){
        //利用QueryWrapper执行查询操作演示
        QueryWrapper<Classroom> query=new QueryWrapper<>();
        // QueryWrapper本身就是一个设置查询条件的对象
        // 设置好查询条件后用于执行查询操作
        // where invite_code='JSD2003-005803'
        query.eq("invite_code","JSD2003-005803");
        // 执行查询
        // 执行查询的对象仍然是对应的Mapper对象,
        //          只是查询时,将query做参数放入查询方法中
        Classroom classroom=classroomMapper.selectOne(query);
        System.out.println(classroom);
    }

    @Autowired
    IUserService userService;
    @Test
    public void reg(){
        RegisterVo registerVo=new RegisterVo();
        registerVo.setInviteCode("JSD2003-005803");
        registerVo.setPhone("13011010033");
        registerVo.setNickname("二龙");
        registerVo.setPassword("123123");
        userService.registerStudent(registerVo);
    }

    @Test
    public void t(){
        UserVo userVo = new UserVo();
        if (!userVo.equals("")){
            System.out.println("1");
            System.out.println(userVo.toString());
        }else {
            System.out.println("2");
        }

    }

}
