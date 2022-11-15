package com.zc.knowsportal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author Cong
 * @ClassName RegisterVo
 * @Description 封装表单
 * @Date 15/11/2022  下午 7:19
 */
@Data
public class RegisterVo {
    // 这些属性的属性名 必须和表单中那么属性值完全一致

    //@NotBlank表示当前属性值不允许为空,
    //message指定的值是当前属性如果为空发生的错误信息
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @NotBlank(message = "手机号不能为空")
    //@Pattern表示当前属性需要正则表达式判断格式
    // regexp指定正则表达式
    // message仍然是验证不通过时的错误信息
    @Pattern(regexp="^1\\d{10}$",message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "昵称是2~20个字符")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",
            message = "密码必须是6~20个字母数字或_")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirm;
}
