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
 * 支付记录表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_payment_record")
public class TPaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 支付平台交易编号
     */
    private String tradeNo;

    /**
     * 支付类型：支付宝/微信
     */
    private String payType;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 实际支付金额 (单位：分)
     */
    private Integer payFee;

    /**
     * 支付平台回调参数
     */
    private String notifyParams;

    /**
     * 支付记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 支付记录更新时间
     */
    private LocalDateTime updateTime;
}
