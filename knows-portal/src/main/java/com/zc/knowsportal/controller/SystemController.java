package com.zc.knowsportal.controller;

import com.zc.knowsportal.exception.ServiceException;
import com.zc.knowsportal.service.impl.UserServiceImpl;
import com.zc.knowsportal.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @Author Cong
 * @ClassName SystemController
 * @Description 系统工具 |
 * //
 * @Date 15/11/2022  下午 7:52
 */
@RestController
//下面的注解是lombok提供的,这个注解的作用是会自动在当前类中声明一个log对象,该对象可以记录日志
@Slf4j
public class SystemController {

    @Autowired
    private UserServiceImpl userService ;

    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo,BindingResult result){
            log.debug("接收到注册信息:{}",registerVo);
            //判读验证后是否有错误
            if(result.hasErrors()){
                // 接收错误信息
                String msg=result.getFieldError().getDefaultMessage();
                // 返回错误信息
                return msg;
            }
            //控制层调用业务逻辑层对象的方法
            try {
                userService.registerStudent(registerVo);
                return "测试完成";
            }catch (ServiceException e){
                log.error("注册失败!",e);
                return e.getMessage();
            }
    }
    /**
     * 读取配置文件中的路径信息
     */
    @org.springframework.beans.factory.annotation.Value("${knows.resource.path}")
    private File resourcePath;

    @Value("${knows.resource.host}")
    private String resourceHost;

    @PostMapping("/upload/image")
    public String uploadImage(MultipartFile imageFile) throws IOException {
        //  确定要上传的路径
        // 2021/09/28
        String path= DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDate.now());
        //  确定整体路径:D:/upload/2021/09/28
        File folder=new File(resourcePath,path);
        //  创建路径以确保路径存在
        folder.mkdirs(); //mkdirssssssssss!!!!!
        //  创建随机的文件名
        String name= UUID.randomUUID().toString();
        //  确定扩展名
        String original=imageFile.getOriginalFilename();
        //   aj.a.de.jpg
        //   01234567890
        String ext=original.substring(
                original.lastIndexOf("."));
        String fullName=name+ext;
        log.debug("即将上传的文件名为:{}",fullName);
        //  创建上传文件路径的File对象
        File file=new File(folder,fullName);
        //  执行保存文件到服务器硬盘
        imageFile.transferTo(file);
        //  拼接一个可以访问静态资源的url
        //  http://localhost:8899/2021/09/28/xxxx.jpg
        String url=resourceHost+"/"+path+"/"+fullName;

        //  返回上传信息
        return url;
    }
}
