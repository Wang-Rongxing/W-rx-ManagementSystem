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
 * 订单信息表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_order_info")
public class TOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 下单用户 id
     */
    private Long userId;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 关联产品 id
     */
    private Long productId;

    /**
     * 订单金额 (单位：分)
     */
    private Integer totalFee;

    /**
     * 订单支付二维码链接
     */
    private String qrcodeUrl;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否完成：1-是，0-否
     */
    private Boolean isCompleted;
}
