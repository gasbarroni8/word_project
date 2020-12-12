package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Article
 * 文章
 * @author ZhengChaoHui
 * @date 2020/12/12 20:55
 */
@Data
@TableName(value = "article")
public class Article {

    /**
     * 文章id（UUID）
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 链接
     */
    private String link;

    /**
     * 图片url
     */
    @TableField(value = "img_url")
    private String imgUrl;
}
