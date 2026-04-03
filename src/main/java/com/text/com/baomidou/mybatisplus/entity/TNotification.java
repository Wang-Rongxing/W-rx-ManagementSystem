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
 * 系统通知表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_notification")
public class TNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型：1-系统通知，2-用户通知
     */
    private Boolean notifyType;

    /**
     * 接收人用户 id，0 表示全部用户
     */
    private Integer receiveUserId;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Boolean isRead;

    /**
     * 通知创建时间
     */
    private LocalDateTime createTime;

    /**
     * 通知更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 通知发送人
     */
    private String sendUser;

    /**
     * 通知状态：0-草稿，1-已发送
     */
    private Boolean notifyStatus;
}
