package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 支付参数配置表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_pay_config")
public class TPayConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付宝 app_id
     */
    private String alipayAppId;

    /**
     * 支付宝应用私钥
     */
    private String alipayPrivateKey;

    /**
     * 支付宝应用公钥
     */
    private String alipayPublicKey;

    /**
     * 支付宝平台公钥
     */
    private String alipayPlatformPublicKey;

    /**
     * 支付宝异步回调地址
     */
    private String alipayNotifyUrl;

    /**
     * 支付宝同步回调地址
     */
    private String alipayReturnUrl;

    /**
     * 支付宝网关请求地址
     */
    private String alipayGatewayUrl;

    /**
     * 支付宝订单付款超时时间
     */
    private String alipayTimeoutExpress;

    /**
     * 微信支付商户号
     */
    private String wechatMchId;

    /**
     * 微信支付商户 API 证书序列号
     */
    private String wechatMchSerialNo;

    /**
     * 微信支付商户私钥文件路径
     */
    private String wechatPrivateKeyPath;

    /**
     * 微信支付 APIv3 密钥
     */
    private String wechatApiV3Key;

    /**
     * 微信支付 APPID
     */
    private String wechatAppId;

    /**
     * 微信支付网关地址
     */
    private String wechatGatewayUrl;

    /**
     * 微信支付回调通知地址
     */
    private String wechatNotifyUrl;

    /**
     * 微信支付 APIv2 密钥
     */
    private String wechatApiV2Key;
}
