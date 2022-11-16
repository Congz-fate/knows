package com.zc.knowsportal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.mapper.ClassroomMapper;
import com.zc.knowsportal.mapper.UserRoleMapper;
import com.zc.knowsportal.model.Classroom;
import com.zc.knowsportal.model.User;
import com.zc.knowsportal.mapper.UserMapper;
import com.zc.knowsportal.model.UserRole;
import com.zc.knowsportal.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zc.knowsportal.vo.RegisterVo;
import com.zc.knowsportal.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类（User
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private UserCollectServiceImpl userCollectService ;

    @Override
    public void registerStudent(RegisterVo registerVo) {
        //1.按用户输入的邀请码查询班级
        QueryWrapper<Classroom> query=new QueryWrapper<>();
        query.eq("invite_code",registerVo.getInviteCode());
        Classroom classroom=classroomMapper.selectOne(query);
        //2.判断班级是否存在,如果不存在发生异常
        if(classroom==null){
            // 班级不存在,表示邀请码错误,抛出异常
            throw new ServiceException("邀请码错误");
        }
        //3.查询用户信息,根据用户输入的手机号查询
        User user=userMapper
                .findUserByUsername(registerVo.getPhone());
        //4.判断用户是否存在,如果存在发生异常
        if(user!=null){
            throw new ServiceException("手机号已经注册过");
        }
        //5.密码加密
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String pwd="{bcrypt}"+encoder
                .encode(registerVo.getPassword());
        //6.实例化User对象,并赋值
        User stu=new User()
                .setUsername(registerVo.getPhone())
                .setNickname(registerVo.getNickname())
                .setPassword(pwd)
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setEnabled(1)
                .setLocked(0)
                .setType(0);
        //7.执行User新增
        int num=userMapper.insert(stu);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        //8.执行UserRole关系的新增
        UserRole userRole=new UserRole()
                //stu的id是新增成功时框架赋值的
                .setUserId(stu.getId())
                .setRoleId(2);
        num=userRoleMapper.insert(userRole);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
    }

    /**
     * 声明包含所有讲师的List缓存和Map缓存对象
     */
    private List<User> teachers=new CopyOnWriteArrayList<>();
    private Map<String,User> teacherMap=new ConcurrentHashMap<>();

    @Override
    public List<User> getTeachers() {
        if(teachers.isEmpty()){
            synchronized (teachers){
                if(teachers.isEmpty()){
                    QueryWrapper<User> query=new QueryWrapper<>();
                    query.eq("type",1);
                    List<User> users=userMapper.selectList(query);
                    // 将查询到的所有讲师保存到list和map
                    teachers.addAll(users);
                    for(User u: users){
                        teacherMap.put(u.getNickname(),u);
                    }
                    log.debug("缓存保存完成!");
                }
            }
        }
        //最后的返回值一定不能是null!!!
        return teachers;
    }

    @Override
    public Map<String, User> getTeacherMap() {
        if(teacherMap.isEmpty()){
            getTeachers();
        }
        return teacherMap;
    }

    @Override
    public UserVo getUserVo(String username) {
        // 根据用户名查询UserVo基本信息
        UserVo userVo=userMapper.findUserVoByUsername(username);
        // 根据用户id获得用户问题数
        Integer questions=questionService.countQuestionsByUserId(userVo.getId());
        // 根据用户id获得用户收藏数
        Integer collect = userCollectService.countCollectByUserId(userVo.getId());
        // 将查询出的问题数赋值到userVo
        userVo.setQuestions(questions);
        userVo.setCollections(collect);
        return userVo;
    }
}
