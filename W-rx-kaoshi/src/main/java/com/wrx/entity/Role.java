package com.wrx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Getter
@Setter
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("role_key")
    private String roleKey;
    
    // 提供getter和setter方法以保持兼容性
    public Integer getRoleId() {
        return id;
    }
    
    public void setRoleId(Integer roleId) {
        this.id = roleId;
    }
}
