package com.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String avatar;

    /**
     * 角色：0-普通用户，1-管理员
     */
    private Integer role;
}
