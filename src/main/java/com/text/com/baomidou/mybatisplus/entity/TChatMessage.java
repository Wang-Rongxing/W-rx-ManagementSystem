package com.baomidou.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户消息表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_chat_message")
public class TChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 发送人用户 id
     */
    private Integer sendUserId;

    /**
     * 接收人用户 id
     */
    private Integer receiveUserId;
}
