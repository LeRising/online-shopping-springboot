package com.mall.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求 DTO
 *
 * <p>用户注册时的请求参数，包含用户名、密码和可选的昵称。</p>
 * <p>邮箱和手机号可在登录后通过个人中心绑定。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class RegisterDTO {

    /** 用户名（3-20 个字符） */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20个字符")
    private String username;

    /** 密码（6-20 个字符） */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20个字符")
    private String password;

    /** 昵称（可选，默认为用户名） */
    private String nickname;
}
