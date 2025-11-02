package com.wrx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 入住登记表
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Getter
@Setter
@TableName("check_in")
public class CheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入住记录ID
     */
    @TableId(value = "check_in_id", type = IdType.AUTO)
    private Integer checkInId;

    /**
     * 关联客户ID
     */
    private Integer customerId;

    /**
     * 关联客房ID
     */
    private Integer roomId;

    /**
     * 实际入住时间
     */
    private LocalDateTime actualCheckIn;

    /**
     * 退房时间
     */
    private LocalDateTime actualCheckOut;


}
