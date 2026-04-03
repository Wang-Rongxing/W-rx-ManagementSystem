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
 * 圈子帖子表
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Getter
@Setter
@TableName("t_square")
public class TSquare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子主键 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属栏目 id
     */
    private Integer classId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子媒体资源地址
     */
    private String mediaUrl;

    /**
     * 帖子类型
     */
    private Integer postType;

    /**
     * 标题颜色
     */
    private String titleColor;

    /**
     * 标题字体大小
     */
    private String titleFontSize;

    /**
     * 标题字体样式
     */
    private Integer titleFontType;

    /**
     * 帖子正文内容
     */
    private String content;

    /**
     * 帖子点赞数
     */
    private Integer likeNum;

    /**
     * 帖子作者 id
     */
    private Integer authorId;

    /**
     * 帖子来源
     */
    private String source;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 跳转链接
     */
    private String redirectUrl;

    /**
     * 帖子关键字
     */
    private String keyword;

    /**
     * 帖子点击数
     */
    private Integer clickNum;

    /**
     * 帖子评论数
     */
    private Integer commentNum;

    /**
     * 是否允许评论：1-允许，0-禁止
     */
    private Boolean isComment;

    /**
     * 是否置顶：1-是，0-否
     */
    private Boolean isTop;

    /**
     * 是否推荐：1-是，0-否
     */
    private Boolean isElite;

    /**
     * 是否删除：1-是，0-否
     */
    private Boolean isDeleted;

    /**
     * 帖子添加时间
     */
    private LocalDateTime addTime;

    /**
     * 帖子更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 帖子生成时间
     */
    private LocalDateTime generateTime;

    /**
     * 最后评论时间
     */
    private LocalDateTime lastCommentTime;

    /**
     * 帖子重要性标签
     */
    private Integer priorityTag;

    /**
     * 自定义备注
     */
    private String customRemark;

    /**
     * 帖子静态页地址
     */
    private String htmlPath;

    /**
     * 附件上传地址
     */
    private String filePath;

    /**
     * 模板文件地址
     */
    private String templatePath;

    /**
     * 帖子图片地址
     */
    private String postImg;

    /**
     * 帖子缩略图地址
     */
    private String thumbImg;

    /**
     * 静态页生成状态
     */
    private String generateStatus;
}
