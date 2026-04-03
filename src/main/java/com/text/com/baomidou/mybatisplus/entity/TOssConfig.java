package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 对象存储配置表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_oss_config")
public class TOssConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * OSS 访问域名
     */
    private String ossDomain;

    /**
     * 腾讯云 COS 存储桶名称
     */
    private String cosBucketName;

    /**
     * 腾讯云 COS SecretId
     */
    private String cosSecretId;

    /**
     * 腾讯云 COS SecretKey
     */
    private String cosSecretKey;

    /**
     * 腾讯云 COS 存储桶区域
     */
    private String cosRegion;

    /**
     * 是否启用腾讯云 COS：1-是，0-否
     */
    private Boolean isCosEnable;

    /**
     * 存储类型：1-COS，2-七牛云
     */
    private Boolean storageType;

    /**
     * 七牛云 AccessKey
     */
    private String qiniuAccessKey;

    /**
     * 七牛云 SecretKey
     */
    private String qiniuSecretKey;

    /**
     * 七牛云存储桶名称
     */
    private String qiniuBucketName;

    /**
     * 七牛云访问域名
     */
    private String qiniuDomain;
}
