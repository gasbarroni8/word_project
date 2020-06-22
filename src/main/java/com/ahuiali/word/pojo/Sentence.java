package com.ahuiali.word.pojo;

import java.io.Serializable;

/**
 * Created by shkstart on 2019/10/6
 * @author ahui
 */
public class Sentence implements Serializable {

    private Integer id;

    private String sentence_en;

    private String sentence_cn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSentence_en() {
        return sentence_en;
    }

    public void setSentence_en(String sentence_en) {
        this.sentence_en = sentence_en;
    }

    public String getSentence_cn() {
        return sentence_cn;
    }

    public void setSentence_cn(String sentence_cn) {
        this.sentence_cn = sentence_cn;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + id +
                ", sentence_en='" + sentence_en + '\'' +
                ", sentence_cn='" + sentence_cn + '\'' +
                '}';
    }
}
