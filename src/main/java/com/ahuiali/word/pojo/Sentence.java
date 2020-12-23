package com.ahuiali.word.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by shkstart on 2019/10/6
 * @author ahui
 */
@Data
public class Sentence implements Serializable {

    private Integer id;

    private String sentenceEn;

    private String sentenceCn;
}
