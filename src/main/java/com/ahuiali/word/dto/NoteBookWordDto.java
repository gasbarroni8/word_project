package com.ahuiali.word.dto;

import lombok.Data;

import java.util.Date;

/**
 * NoteBookWordDto
 * 生词本单词dto
 * @author ZhengChaoHui
 * @date 2021/1/13 0:19
 */
@Data
public class NoteBookWordDto extends WordDto{

    /**
     * 下次复习
     */
    private Date nextTime;

    /**
     * 复习次数
     */
    private Integer reviewCount;

    /**
     * 英式发音
     */
    private String pronUk;

    /**
     * 美式发音
     */
    private String pronUs;
}
