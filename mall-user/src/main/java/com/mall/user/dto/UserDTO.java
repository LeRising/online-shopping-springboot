package com.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息 DTO
 *
 * <p>用于返回用户信息，不包含密码字段。</p>
 *
 * @author risinglee
 * @since 1.0.0
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 头像 URL */
    private String avatar;

    /** 角色：0-普通用户，1-管理员 */
    private Integer role;
}
