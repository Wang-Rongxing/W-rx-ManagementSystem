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
 * 退款记录表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_refund_record")
public class TRefundRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款记录主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 商户退款单编号
     */
    private String refundNo;

    /**
     * 支付平台退款单号
     */
    private String platformRefundNo;

    /**
     * 原订单金额 (单位：分)
     */
    private Integer originalFee;

    /**
     * 退款金额 (单位：分)
     */
    private Integer refundFee;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款申请返回参数
     */
    private String applyRefundParams;

    /**
     * 退款结果回调参数
     */
    private String refundNotifyParams;

    /**
     * 退款记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 退款记录更新时间
     */
    private LocalDateTime updateTime;
}
