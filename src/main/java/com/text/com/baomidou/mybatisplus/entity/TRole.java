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
 * 角色表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_role")
public class TRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色排序号
     */
    private Integer sortNo;

    /**
     * 角色创建时间
     */
    private LocalDateTime createTime;

    /**
     * 角色更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色状态：1-启用，0-停用
     */
    private Boolean isEnable;

    /**
     * 角色备注
     */
    private String remark;
}
