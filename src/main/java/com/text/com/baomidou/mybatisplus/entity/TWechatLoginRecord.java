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
 * 微信登录记录表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_wechat_login_record")
public class TWechatLoginRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录记录主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信登录场景码
     */
    private String sceneCode;

    /**
     * 关联用户 id
     */
    private String userId;

    /**
     * 登录状态
     */
    private String loginStatus;

    /**
     * 登录记录创建时间
     */
    private LocalDateTime createTime;
}
