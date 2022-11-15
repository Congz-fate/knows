package com.zc.knowsportal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @Author Cong
 * @ClassName QuestionVo
 * @Description 问题表单
 * @Date 16/11/2022  上午 12:03
 */
@Data
public class QuestionVo {

    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "问题标题在3~50个字符")
    private String title;
    @NotEmpty(message = "至少选中一个标签")
    private String[] tagNames={};
    @NotEmpty(message = "至少选择一个讲师")
    private String[] teacherNicknames={};
    @NotBlank(message = "问题内容不能为空")
    private String content;
}
