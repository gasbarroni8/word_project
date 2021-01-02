package com.ahuiali.word.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Article
 * 文章
 * @author ZhengChaoHui
 * @date 2020/12/12 20:55
 */
@Data
@TableName(value = "article")
public class Article implements Serializable {

    /**
     * 文章id（UUID）
     */
    private String id;

    /**
     * 标题
     */
    @JSONField(name = "title")
    @TableField(value = "title")
    private String title;

    /**
     * 链接
     */
    @JSONField(name = "url")
    @TableField(value = "url")
    private String url;

    /**
     * 来源
     */
    @TableField(value = "source")
    private String source;

    /**
     * 分类
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 文章日期
     */
    @JSONField(name = "focus_date")
    @TableField(value = "date")
    private Date date;

    /**
     * 描述
     */
    @JSONField(name = "desc")
    @TableField(value = "description")
    private String description;

    /**
     * 图片url
     */
    @JSONField(name = "image")
    @TableField(value = "image")
    private String image;
}
