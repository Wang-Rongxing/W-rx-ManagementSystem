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
 * 圈子评论表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_square_comment")
public class TSquareComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父评论 id，0 为根评论
     */
    private Integer parentId;

    /**
     * 评论类型：1-帖子评论
     */
    private Boolean commentType;

    /**
     * 评论人用户 id
     */
    private Long userId;

    /**
     * 被回复人用户 id
     */
    private Integer replyUserId;

    /**
     * 关联帖子 id
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论点赞数
     */
    private Integer likeNum;

    /**
     * 评论状态：1-正常，0-禁用
     */
    private Boolean isEnable;

    /**
     * 评论创建时间
     */
    private LocalDateTime createTime;
}
