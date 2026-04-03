package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_follow")
public class TFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关注记录主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关注人用户 id
     */
    private Integer followerUserId;

    /**
     * 被关注人用户 id
     */
    private Integer followedUserId;
}
