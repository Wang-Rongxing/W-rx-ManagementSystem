package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_user")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键 id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 微信开放平台 openid
     */
    private String wechatOpenid;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户个人简介
     */
    private String userIntro;

    /**
     * 用户性别：1-男，2-女，0-未知
     */
    private Boolean gender;

    /**
     * 用户自定义昵称
     */
    private String nickname;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 用户登录密码 (加密存储)
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 短信/邮箱验证码
     */
    private Integer verifyCode;

    /**
     * 扩展字段
     */
    private Integer extData;

    /**
     * 用户角色编码
     */
    private String roleCode;

    /**
     * 用户头像地址
     */
    private String avatar;

    /**
     * 用户状态：1-启用，0-禁用
     */
    private Boolean isEnable;

    /**
     * 用户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 会员生效时间
     */
    private LocalDateTime vipStartTime;

    /**
     * 会员过期时间
     */
    private LocalDateTime vipEndTime;

    /**
     * 用户积分
     */
    private Integer integral;

    /**
     * 会员是否禁用：1-是，0-否
     */
    private Boolean isVipDisable;

    /**
     * 用户身高
     */
    private String height;

    /**
     * 是否单身：1-是，0-否
     */
    private Boolean isSingle;

    /**
     * 用户生日
     */
    private String birthday;

    /**
     * 用户学历
     */
    private String education;

    /**
     * 用户月薪
     */
    private String monthlySalary;

    /**
     * 用户常住地
     */
    private String residence;
}
