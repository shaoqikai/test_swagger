package com.example.test_swagger.entity;

import lombok.Data;

/**
 * @author shaoqk
 * @create 2021-01-20 15:11
 */
@Data
public class SysUser {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 微信的OpenID
     */
    private String openId;

    /**
     * 头像地址
     */
    private String imgSrc;
}