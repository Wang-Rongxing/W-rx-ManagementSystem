package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 邮件配置表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_mail_config")
public class TMailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发件人邮箱
     */
    private String sendMail;

    /**
     * 邮箱授权码
     */
    private String authCode;

    /**
     * 邮箱 SMTP 服务器地址
     */
    private String smtpHost;

    /**
     * 邮件协议，默认 smtp
     */
    private String protocol;
}
