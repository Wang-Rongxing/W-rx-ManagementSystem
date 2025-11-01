package com.wrx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 客房表
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Getter
@Setter
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客房唯一ID
     */
    @TableId(value = "room_id", type = IdType.AUTO)
    private Integer roomId;

    /**
     * 客房编号（如301）
     */
    private String roomNumber;

    /**
     * 客房类型（单人间/双人间）
     */
    private String roomType;

    /**
     * 单价（元/天）
     */
    private BigDecimal price;

    /**
     * 状态：1=空闲，2=已预订，3=已入住，4=待清洁
     */
    private Integer status;

    /**
     * 客房描述
     */
    private String description;


}
