package com.wrx.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单数据传输对象，用于向前端返回订单信息
 * </p>
 *
 * @author 系统生成
 * @since 2025-10-28
 */
@Getter
@Setter
public class OrderDto {

    /**
     * 订单唯一ID
     */
    private Integer orderId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客房编号
     */
    private String roomNumber;

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
    private BigDecimal totalPrice;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;
}