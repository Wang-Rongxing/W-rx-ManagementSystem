package com.wrx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Getter
@Setter
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单唯一ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 关联客户ID
     */
    private Integer customerId;

    /**
     * 关联客房ID
     */
    private Integer roomId;

    /**
     * 预计入住日期
     */
    private LocalDateTime checkInDate;

    /**
     * 预计退房日期
     */
    private LocalDateTime checkOutDate;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;


}
