package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 短信服务配置表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_sms_config")
public class TSmsConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 七牛云 AccessKey
     */
    private String qiniuAccessKey;

    /**
     * 七牛云 SecretKey
     */
    private String qiniuSecretKey;

    /**
     * 七牛云短信模板 id
     */
    private String qiniuTemplateId;
}
