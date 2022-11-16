package com.zc.knowsportal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Cong
 * @ClassName UserVo
 * @Date 16/11/2022  下午 2:51
 * @Description 封装用户信息面板的数据
 */
@Data
@Accessors(chain = true)
public class UserVo implements Serializable {
    private Integer id;
    private String username;
    private String nickname;

    /**
     * 问题数
     */
    private int questions;
    /**
     * 收藏数
     */
    private int collections;
}
