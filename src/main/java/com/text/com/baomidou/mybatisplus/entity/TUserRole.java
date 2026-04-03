package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_user_role")
public class TUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId("user_id")
    private Integer userId;

    /**
     * 角色 id
     */
    @TableId("role_id")
    private Integer roleId;
}
