package com.zc.knowsportal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author Cong
 * @ClassName CommentVo
 * @Date 16/11/2022  下午 11:53
 * @Description 评论表单
 */
@Data
@Accessors(chain = true)
public class CommentVo implements Serializable {
    @NotNull(message = "回答ID不能为空")
    private Integer answerId;

    @NotBlank(message = "必须填写评论内容")
    private String content;
}
