package com.ahuiali.word.dto;

import lombok.Data;

/**
 * ParaEnDto
 * 小说段落dto
 * @author ZhengChaoHui
 * @date 2020/12/24 21:35
 */
@Data
public class ParaEnDto {

    private Integer id;

    /**
     * 英文段落
     */
    private String paraEn;
}
