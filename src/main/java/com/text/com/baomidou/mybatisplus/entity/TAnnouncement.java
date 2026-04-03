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
 * 公告表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_announcement")
public class TAnnouncement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告创建时间
     */
    private LocalDateTime createTime;

    /**
     * 公告更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 公告发布人
     */
    private String author;

    /**
     * 公告是否有效：1-有效，0-无效
     */
    private Boolean isActive;
}
