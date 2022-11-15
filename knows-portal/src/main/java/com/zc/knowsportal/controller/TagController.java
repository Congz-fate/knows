package com.zc.knowsportal.controller;


import com.zc.knowsportal.model.Tag;
import com.zc.knowsportal.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zc.com
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/v1/tags")
public class TagController {
    @Autowired
    private ITagService tagService;
    // @GetMapping("")表示访问本方法的路径就是类上声明的路径
    // localhost:8080/v1/tags即可
    @GetMapping("")
    public List<Tag> tags(){
        List<Tag> tags=tagService.getTags();
        return tags;
    }
}
