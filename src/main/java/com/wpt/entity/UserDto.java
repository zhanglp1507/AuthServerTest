package com.wpt.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lihan on 2016/12/22.
 */
@Data
public class UserDto implements Serializable {

    /**
     * Id
     */
    private long id;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * displayName
     */
    private String displayName;

    /**
     * ogAccount
     */
    private String ogAccount;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证时间
     */
    private Date emailValidatedAt;

    /**
     * 用户邮箱验证状态
     */
    private Integer emailValidationStatus;

    /**
     * 密码加密类型
     */
    private Integer passwordEncryptionType;

    /**
     * 密码盐值
     */
    private String  passwordSalt;

    /**
     * 用户密码
     */
    private String encryptedPassword;

    /**
     * 用户状态
     */
    private Integer accountStatus;

    /**
     * 注册ip
     */
    private String regIp;

    /**
     * 注册UA
     */
    private String regUa;

    /**
     * 注册平台类型
     */
    private Integer sourceId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 刷新验证登录状态改变token
     */
    private String authToken;


}
