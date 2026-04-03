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
 * 公告已读记录表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("sys_notice_read")
public class SysNoticeRead implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 已读主键
     */
    @TableId(value = "read_id", type = IdType.AUTO)
    private Long readId;

    /**
     * 公告id
     */
    private Integer noticeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
}
