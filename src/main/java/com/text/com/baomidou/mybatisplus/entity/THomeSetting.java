package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 首页设置表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_home_setting")
public class THomeSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 首页特色板块标题
     */
    private String featureTitle;

    /**
     * 首页特色板块图片
     */
    private String featureImg;

    /**
     * 首页渐变左颜色
     */
    private String gradientColorLeft;

    /**
     * 首页渐变右颜色
     */
    private String gradientColorRight;

    /**
     * 特色板块是否启用：1-是，0-否
     */
    private Boolean isFeatureEnable;
}
