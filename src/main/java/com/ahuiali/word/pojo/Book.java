package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName(value = "book")
public class Book {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String title;

    private String img;

    private String author;

    /**
     * 分类
     */
    private String tag;

    /**
     * 长度
     */
    private String length;

    /**
     * 难度
     */
    private String hard;

    //是否推荐，0否1是
    @TableField(value = "is_hot")
    private Integer isHot;

    private String summary;

    @TableField(value = "book_index")
    private Integer indexBook;

    @TableField(value = "latest_loc")
    private String latestLoc;

    private List<Chapter> chapters;
}
