package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "book_chapter")
public class Chapter implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField(value = "chapter_name")
    private String chapterName;

    @TableField(value = "book_index")
    private Integer bookIndex;

    @TableField(value = "chapter_index")
    private Integer chapterIndex;

}
