package com.zc.knowsportal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author Cong
 * @ClassName AnswerVo
 * @Date 16/11/2022  下午 10:40
 * @Description 回答类表单
 */
@Data
public class AnswerVo implements Serializable {

    @NotNull(message = "问题编号不能为空")
    private Integer questionId;

    @NotBlank(message = "必须填写回答内容")
    private String content;
}
