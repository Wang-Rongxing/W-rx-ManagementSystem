package com.wrx.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * <p>
 * 入住登记数据传输对象，用于向前端返回入住登记信息
 * </p>
 *
 * @author 系统生成
 * @since 2025-10-28
 */
@Getter
@Setter
public class CheckInDto {

    /**
     * 入住登记唯一ID
     */
    private Integer checkInId;

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
     * 入住日期
     */
    private LocalDateTime actualCheckIn;

    /**
     * 退房日期
     */
    private LocalDateTime actualCheckOut;

}
